package ua.skidchenko.registrationform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.skidchenko.registrationform.entity.User;


public interface UserRepository extends JpaRepository<User,Long> {
}
