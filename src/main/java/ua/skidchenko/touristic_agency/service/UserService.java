package ua.skidchenko.touristic_agency.service;

import ua.skidchenko.touristic_agency.dto.UserDTO;
import ua.skidchenko.touristic_agency.entity.User;


public interface UserService {
    User saveUser(UserDTO userDTO);

    User getUserByUsername(String username);

    User chargeUserWallet(Long amountOfCharge, String username);
}
