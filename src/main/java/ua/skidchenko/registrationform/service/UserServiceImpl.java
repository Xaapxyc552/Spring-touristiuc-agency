package ua.skidchenko.registrationform.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.skidchenko.registrationform.dto.UserDTO;
import ua.skidchenko.registrationform.entity.User;
import ua.skidchenko.registrationform.repository.UserRepository;

import java.util.List;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    final
    UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User saveUser(UserDTO userDTO) {
        log.info("Saving user into DB. User data: " + userDTO.toString());
        User userToSave = User.builder().
                password(userDTO.getPassword()).
                firstname(userDTO.getFirstname()).
                email(userDTO.getEmail()).
                login(userDTO.getLogin()).
                build();
        return repository.save(userToSave);
    }

    @Override
    public List<User> readAllUsersFromDB() {
        List<User> allUsersFromDb = repository.findAll();
        log.info("Retrieved from DB all users: " + allUsersFromDb.toString());
        return allUsersFromDb;
    }
}
