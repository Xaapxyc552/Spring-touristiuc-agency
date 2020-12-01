package ua.skidchenko.touristic_agency.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.skidchenko.touristic_agency.dto.UserDTO;
import ua.skidchenko.touristic_agency.entity.enums.Role;
import ua.skidchenko.touristic_agency.entity.User;
import ua.skidchenko.touristic_agency.exceptions.UsernameExistsExcetion;
import ua.skidchenko.touristic_agency.repository.UserRepository;
import ua.skidchenko.touristic_agency.service.UserService;

import java.util.Optional;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    final
    UserRepository userRepository;

    final
    BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(@Lazy UserRepository repository,
                           @Lazy BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(UserDTO userDTO) {
        log.info("Saving user into DB. User data: " + userDTO.toString());
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            log.warn("Username " + userDTO.getUsername() + " already exists in system;");
            throw new UsernameExistsExcetion("Username " + userDTO.getUsername() + " already exists in system;");
        }
        return userRepository.save(buildUserFromDTO(userDTO));
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("Retrieving user by username. Username: " + username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(() -> {
            log.warn("User with username " + username + " not found in DB. Exception thrown.");
            return new UsernameNotFoundException("User with username " + username + " was not found.");
        });

    }

    @Override
    @Transactional
    public User chargeUserWallet(Long amountOfCharge, String username) {
        log.info("Starting recharging user`s account. Amount: " + amountOfCharge + ". Username: " + username);
        User user = userRepository.findByUsernameAndRole(username, Role.ROLE_USER).orElseThrow(() -> {
            log.warn("User not present in DB. Recharging interrupted. Username: " + username);
            throw new UsernameNotFoundException("User not found in DB. Username: " + username);
        });
        user.setMoney(user.getMoney() + amountOfCharge);
        return userRepository.save(user);
    }

    private User buildUserFromDTO(UserDTO userDTO) {
        log.info("Building new user from userDTO. UserDTO: " + userDTO.toString());
        return User.builder()
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .firstname(userDTO.getFirstname())
                .role(Role.ROLE_USER)
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .money(0L)
                .build();
    }

}
