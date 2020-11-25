package ua.skidchenko.registrationform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.skidchenko.registrationform.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
