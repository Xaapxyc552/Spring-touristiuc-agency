package ua.skidchenko.touristic_agency.controller;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.touristic_agency.entity.Check;
import ua.skidchenko.touristic_agency.entity.User;
import ua.skidchenko.touristic_agency.exceptions.NotPresentInDatabaseException;
import ua.skidchenko.touristic_agency.service.client_services.UserBookingService;
import ua.skidchenko.touristic_agency.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {

    final
    UserService userService;

    final
    UserBookingService bookingService;

    public UserController(UserService userService,
                          UserBookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    //прописать тайминги логированием

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/personal-account/{page}")
    public String personalAccountPage(@NotNull Principal principal,
                                      @PathVariable(name = "page") int page,
                                      Model model) {
        String username = principal.getName();
        User userFromDatabase = userService.getUserByUsername(username);                            //TODO User (сущность) в контроллере. можно ли так делать? или юзать ДТО?
        log.info("Retrieving user information to display at personal-account page. Username: " + username);
        Page<Check> userChecks = bookingService
                .findAllChecksByUsernameOrderByStatus(principal.getName(), page - 1);
        List<Integer> pagesSequence = IntStream
                .rangeClosed(1, userChecks.getTotalPages())
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("email", userFromDatabase.getEmail());
        model.addAttribute("firstname", userFromDatabase.getFirstname());
        model.addAttribute("money", userFromDatabase.getMoney());
        model.addAttribute("userChecks", userChecks.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pagesSequence", pagesSequence);
        return "personalAccount";
    }

    @PostMapping("/recharge")
    public String rechargeUserWallet(@RequestParam(name = "amountOfCharge") Long amountOfCharge,
                                     Principal principal) {
        String username = principal.getName();
        log.info("Starting recharging user`s account. Amount: " + amountOfCharge + ". Username: " + username);
        if (amountOfCharge <= 0) {
            throw new IllegalArgumentException("Amount to charge cannot be zero or negative.");
        }
        userService.chargeUserWallet(amountOfCharge, username);
        return "redirect:/user/personal-account/1";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class,
            NotPresentInDatabaseException.class,
            UsernameNotFoundException.class})
    public String handleException(Model model, RuntimeException ex) {
        log.warn("Handling exception. Exception: " + ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

}
