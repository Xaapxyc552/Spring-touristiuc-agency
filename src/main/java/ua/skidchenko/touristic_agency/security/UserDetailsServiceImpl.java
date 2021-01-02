package ua.skidchenko.touristic_agency.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ua.skidchenko.touristic_agency.entity.User;
import ua.skidchenko.touristic_agency.exceptions.UsernameNotFoundException;
import ua.skidchenko.touristic_agency.repository.UserRepository;

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
        log.info("Retrieved by Spring Security user from UserService: " + user);
        return user;
    }
}
