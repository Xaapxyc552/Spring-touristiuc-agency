package ua.skidchenko.registrationform.service;

import ua.skidchenko.registrationform.dto.UserDTO;
import ua.skidchenko.registrationform.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(UserDTO userDTO);
    User getUserByUsername(String username);
    List<User> readAllUsersFromDB();
}
