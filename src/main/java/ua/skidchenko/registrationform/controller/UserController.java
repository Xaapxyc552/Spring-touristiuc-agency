package ua.skidchenko.registrationform.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.registrationform.controller.exceptions.WrongFormInputDataException;
import ua.skidchenko.registrationform.dto.UserDTO;
import ua.skidchenko.registrationform.entity.User;
import ua.skidchenko.registrationform.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/")
public class UserController {

    final
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //прописать тайминги логированием

    @GetMapping("/main")
    public String mainPage() {
        log.info("Main page was visited.");
        return "mainPage";
    }

    @GetMapping("/new-user")
    public String newUserPage() {
        log.info("User register page was visited.");
        return "userRegisterPage";
    }

    @ResponseStatus()
    @PostMapping("/new-user")
    public String createNewUser(@Valid UserDTO userDTO,
                                BindingResult bindingResult) {
        //проверить варианты
        if (bindingResult.hasErrors()) {
            log.warn("Exception during validation of UserDTO" + userDTO.toString());
            throw new WrongFormInputDataException("Entered incorrect data.", getValidationErrors(bindingResult));
        }
        log.info("Saving into DB new user. User data: " + userDTO.toString());
        userService.saveUser(userDTO);
        return "userSuccessfullyRegistered";
        //отправлять 200-й код вместо вью (посмотреть у тренера)
    }
    //response entity используется для отправки сообщений о результатах работы на браузер клиента


    //получить данные по юзеру (новй метод)

    @GetMapping("/all-users")
    public String displayAllUsers(Model model) {
        List<User> usersToDisplay = userService.readAllUsersFromDB();
        log.info("Retrieved from DB data about all users:" + usersToDisplay.toString());
        model.addAttribute("usersToDisplay", usersToDisplay);
        return "displayAllUsers";
    }

    @ExceptionHandler(WrongFormInputDataException.class)
    public String handleException(WrongFormInputDataException exception, Model model) {
        log.info("Starting handling exception: " + exception.getErrors());
        model.addAttribute("errors", exception.getErrors());
        return "errors";
    }


    @org.jetbrains.annotations.NotNull
    private List<String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult.
                getAllErrors().
                stream().
                map(ObjectError::getDefaultMessage).
                collect(Collectors.toList());
    }

}
