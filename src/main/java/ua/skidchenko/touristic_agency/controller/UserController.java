package ua.skidchenko.touristic_agency.controller;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.touristic_agency.entity.Check;
import ua.skidchenko.touristic_agency.entity.User;
import ua.skidchenko.touristic_agency.exceptions.IncorrectSumm;
import ua.skidchenko.touristic_agency.exceptions.NotPresentInDatabaseException;
import ua.skidchenko.touristic_agency.exceptions.PropertyLocalizedException;
import ua.skidchenko.touristic_agency.exceptions.UsernameNotFoundException;
import ua.skidchenko.touristic_agency.service.client_services.UserBookingService;
import ua.skidchenko.touristic_agency.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${dollar.course}")
    private Double dollarCourse;

    private static final int AMOUNT_OF_KOPECKS_IS_HRYVNA = 100;

    final
    UserService userService;

    final
    UserBookingService bookingService;

    private final MessageSource messageSource;

    public UserController(UserService userService,
                          UserBookingService bookingService, MessageSource messageSource) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/personal-account/{page}")
    public String personalAccountPage(@NotNull Principal principal,
                                      @PathVariable(name = "page") int page,
                                      Model model) {
        String username = principal.getName();
        User userFromDatabase = userService.getUserByUsername(username);
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
        model.addAttribute("dollarCourse", dollarCourse);

        return "personalAccount";
    }

    @PostMapping("/recharge")
    public String rechargeUserWallet(@RequestParam(name = "amountOfCharge") Long amountOfCharge,
                                     Principal principal,
                                     @CookieValue(name = "lang")String lang) {
        String username = principal.getName();
        amountOfCharge*=AMOUNT_OF_KOPECKS_IS_HRYVNA;
        log.info("Starting recharging user`s account. Amount: " + amountOfCharge + ". Username: " + username);
        if (amountOfCharge <= 0) {
            throw new IncorrectSumm("Amount to charge cannot be zero or negative.");
        }
        if(lang.equals("en-GB")){
            amountOfCharge=(long)(amountOfCharge*dollarCourse);
        }
        userService.chargeUserWallet(amountOfCharge, username);
        return "redirect:/user/personal-account/1";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IncorrectSumm.class,
            NotPresentInDatabaseException.class,
            UsernameNotFoundException.class})
    public String handleException(Model model,
                                  PropertyLocalizedException ex,
                                  Locale locale) {
        log.warn("Handling exception in ManagerController. Exception: " + ex.getMessage());
        model.addAttribute("error", messageSource.getMessage(
                ex.getPropertyExceptionCode(),null,locale));
        return "error";
    }

}
