package ua.skidchenko.registrationform.service;

import ua.skidchenko.registrationform.dto.UserDTO;
import ua.skidchenko.registrationform.model.User;

import java.util.List;

public interface UserService {
    User saveUser(UserDTO userDTO);
    List<User> readAllUsersFromDB();
}
