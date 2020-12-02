package ua.skidchenko.touristic_agency.controller;


import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.touristic_agency.controller.exceptions.WrongFormInputDataException;
import ua.skidchenko.touristic_agency.dto.UserDTO;
import ua.skidchenko.touristic_agency.exceptions.UsernameExistsExcetion;
import ua.skidchenko.touristic_agency.service.TourService;
import ua.skidchenko.touristic_agency.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@Log4j2
@RequestMapping("/signup")
public class SignUpController {

    final
    UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
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
        model.addAttribute("href","/main/new");
        model.addAttribute("hrefDescription","user.registered.href_description");
        return "singleMessagePage";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UsernameNotFoundException.class,
            UsernameExistsExcetion.class})
    public String handleOtherExceptions(RuntimeException exception, Model model) {
        log.warn("Handling exception: " + exception.getMessage());
        model.addAttribute("error", exception.getMessage());
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
