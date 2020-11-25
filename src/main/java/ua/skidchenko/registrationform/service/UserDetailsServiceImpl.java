package ua.skidchenko.registrationform.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.skidchenko.registrationform.entity.User;
import ua.skidchenko.registrationform.repository.UserRepository;

@Service
@Log4j2
@Qualifier("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    final
    UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Looking for user with username " + username + " in DB.");
        User user = repository.findByUsername(username).orElseThrow(() -> {
            log.warn("User with username " + username + " not found in DB. Exception thrown.");
            return new UsernameNotFoundException("User with username " + username + " was not found.");
        });
        log.info(user);
        return user;
    }
}
