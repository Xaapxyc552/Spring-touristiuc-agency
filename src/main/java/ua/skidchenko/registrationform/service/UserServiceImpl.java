package ua.skidchenko.registrationform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.skidchenko.registrationform.dto.UserDTO;
import ua.skidchenko.registrationform.model.User;
import ua.skidchenko.registrationform.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    final
    UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User saveUser(UserDTO userDTO) {
        User userToSave = User.builder().
                password(userDTO.getPassword()).
                firstname(userDTO.getFirstname()).
                email(userDTO.getEmail()).
                build();
        return repository.save(userToSave);
    }

    @Override
    public List<User> readAllUsersFromDB() {
        return repository.findAll();
    }
}
