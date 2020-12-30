package ua.skidchenko.touristic_agency.controller;


import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.touristic_agency.controller.exceptions.WrongFormInputDataException;
import ua.skidchenko.touristic_agency.dto.UserDTO;
import ua.skidchenko.touristic_agency.exceptions.PropertyLocalizedException;
import ua.skidchenko.touristic_agency.exceptions.UsernameExistsException;
import ua.skidchenko.touristic_agency.exceptions.UsernameNotFoundException;
import ua.skidchenko.touristic_agency.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Controller
@Log4j2
@RequestMapping("/signup")
public class SignUpController {

    final
    UserService userService;

    private final MessageSource messageSource;

    public SignUpController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public String newUserPage() {
        log.info("User register page was visited.");
        return "registrationPage";
    }

    @PostMapping("")
    public String createNewUser(@Valid UserDTO userDTO,
                                BindingResult bindingResult) {
        checkValidationErrorsElseException(userDTO, bindingResult);
        log.info("Saving into DB new user. User data: " + userDTO.toString());
        userService.saveUser(userDTO);
        return "redirect:/signup/confirm";
    }

    private void checkValidationErrorsElseException(UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Exception during validation of UserDTO" + userDTO.toString());
            throw new WrongFormInputDataException("Entered incorrect data.",
                    getValidationErrors(bindingResult));
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/confirm")
    public String userSuccessfullyRegistered(Model model) {
        log.info("Redirected to PRG-page.");
        model.addAttribute("message","user.registered");
        model.addAttribute("href","/signup");
        model.addAttribute("hrefDescription","user.registered.href_description");
        return "singleMessagePage";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UsernameNotFoundException.class,
            UsernameExistsException.class})
    public String handleException(Model model,
                                  PropertyLocalizedException ex,
                                  Locale locale) {
        log.warn("Handling exception in SignUp controller. Exception: " + ex.getMessage());
        model.addAttribute("error", messageSource.getMessage(
                ex.getPropertyExceptionCode(),null,locale));
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({WrongFormInputDataException.class})
    public String handleValidationExceptions(WrongFormInputDataException exception, Model model) {
        log.warn("Handling exception: " + exception.getErrors());
        model.addAttribute("errors", exception.getErrors());
        return "validationErrors";
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
