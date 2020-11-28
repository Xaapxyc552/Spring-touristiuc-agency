package ua.skidchenko.registrationform.controller;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.registrationform.controller.exceptions.InvalidURLException;
import ua.skidchenko.registrationform.controller.exceptions.WrongFormInputDataException;
import ua.skidchenko.registrationform.dto.UserDTO;
import ua.skidchenko.registrationform.entity.User;
import ua.skidchenko.registrationform.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {

    final
    UserDetailsService userDetailsService;

    final
    UserService userService;

    public UserController(@Qualifier("userDetailsService") UserDetailsService userDetailsService,
                          UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
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
        return "personalAccount";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/new")
    public String newUserPage() {
        log.info("User register page was visited.");
        return "userRegisterPage";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/new")
    public ResponseEntity<String> createNewUser(@Valid UserDTO userDTO,
                                                BindingResult bindingResult) {
        //проверить варианты
        if (bindingResult.hasErrors()) {
            log.warn("Exception during validation of UserDTO" + userDTO.toString());
            throw new WrongFormInputDataException("Entered incorrect data.",
                    getValidationErrors(bindingResult));
        }
        log.info("Saving into DB new user. User data: " + userDTO.toString());
        userService.saveUser(userDTO);
        return new ResponseEntity<>("User successfully registered!", HttpStatus.OK);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all-users")
    public String displayAllUsers(Model model) {
        List<User> usersToDisplay = userService.readAllUsersFromDB();
        log.info("Retrieved from DB data about all users:" + usersToDisplay.toString());
        model.addAttribute("usersToDisplay", usersToDisplay);
        return "displayAllUsers";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({WrongFormInputDataException.class,
            UsernameNotFoundException.class,
            InvalidURLException.class})
    public String handleException(WrongFormInputDataException exception, Model model) {
        log.info("Starting handling exception: " + exception.getErrors());
        model.addAttribute("errors", exception.getErrors());
        return "errors";
    }


    @NotNull
    private List<String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult.
                getAllErrors().
                stream().
                map(ObjectError::getDefaultMessage).
                collect(Collectors.toList());
    }

}
