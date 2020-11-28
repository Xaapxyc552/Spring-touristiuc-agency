package ua.skidchenko.registrationform.controller;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @GetMapping("/personal-account")
    public String enterPersonalAccount(@NotNull Principal principal,
                                       Model model) {
        User userFromDatabase = userService.getUserByUsername(principal.getName());
        model.addAttribute("email", userFromDatabase.getEmail());
        model.addAttribute("firstname", userFromDatabase.getFirstname());
        model.addAttribute("money", userFromDatabase.getMoney());
        List<Check> userChecks = bookingService
                .findAllChecksByUsernameOrderByStatus(principal.getName());
        model.addAttribute("userChecks", userChecks);
        return "personalAccount";
    }



    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all-users")
    public String displayAllUsers(Model model) {
        List<User> usersToDisplay = userService.readAllUsersFromDB();
        log.info("Retrieved from DB data about all users:" + usersToDisplay.toString());
        model.addAttribute("usersToDisplay", usersToDisplay);
        return "displayAllUsers";
    }



}
