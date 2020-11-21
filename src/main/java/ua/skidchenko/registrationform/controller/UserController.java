package ua.skidchenko.registrationform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.skidchenko.registrationform.dto.UserDTO;
import ua.skidchenko.registrationform.model.User;
import ua.skidchenko.registrationform.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    final
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/main")
    public String mainPage() {
        return "mainPage";
    }

    @GetMapping("/new-user")
    public String newUserPage() {
        return "userRegisterPage";
    }

    @PostMapping("/new-user-create")
    public String createNewUser(UserDTO userDTO) {
        userService.saveUser(userDTO);
        return "userSuccessfullyRegistered";
    }

    @GetMapping("/display-all-users")
    public String displayAllUsers(Model model) {
        List<User> usersToDisplay = userService.readAllUsersFromDB();
        model.addAttribute("usersToDisplay", usersToDisplay);
        return "displayAllUsers";

    }


}
