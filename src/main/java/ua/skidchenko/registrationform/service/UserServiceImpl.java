package ua.skidchenko.registrationform.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.skidchenko.registrationform.dto.UserDTO;
import ua.skidchenko.registrationform.entity.enums.Role;
import ua.skidchenko.registrationform.entity.User;
import ua.skidchenko.registrationform.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    final
    UserRepository repository;

    BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(@Lazy UserRepository repository
            , @Lazy BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(UserDTO userDTO) {
        log.info("Saving user into DB. User data: " + userDTO.toString());
        return repository.save(buildUserFromDTO(userDTO));
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> userOptional = repository.findByUsername(username);
        return userOptional.orElseThrow(() -> {
            log.warn("User with username " + username + " not found in DB. Exception thrown.");
            return new UsernameNotFoundException("User with username " + username + " was not found.");
        });

    }

    @Override
    public List<User> readAllUsersFromDB() {
        List<User> allUsersFromDb = repository.findAll();
        log.info("Retrieved from DB all users: " + allUsersFromDb.toString());
        return allUsersFromDb;
    }

    private User buildUserFromDTO(UserDTO userDTO) {
        return User.builder().
                password(passwordEncoder.encode(userDTO.getPassword()))
                .firstname(userDTO.getFirstname())
                .role(Role.ROLE_USER)
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .money(10000L)  //TODO
                .build();
    }
}
