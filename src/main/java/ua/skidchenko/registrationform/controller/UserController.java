package ua.skidchenko.registrationform.controller;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.registrationform.entity.Check;
import ua.skidchenko.registrationform.entity.User;
import ua.skidchenko.registrationform.service.BookingService;
import ua.skidchenko.registrationform.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {

    final
    UserDetailsService userDetailsService;

    final
    UserService userService;

    final
    BookingService bookingService;

    public UserController(@Qualifier("userDetailsService") UserDetailsService userDetailsService,
                          UserService userService, BookingService bookingService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    //прописать тайминги логированием

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/personal-account/{page}")                 //TODO нужна ли тут @Transactional?
    public String personalAccountPage(@NotNull Principal principal,
                                      @PathVariable(name = "page") int page,
                                      Model model) {
        User userFromDatabase = userService.getUserByUsername(principal.getName());
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


}
