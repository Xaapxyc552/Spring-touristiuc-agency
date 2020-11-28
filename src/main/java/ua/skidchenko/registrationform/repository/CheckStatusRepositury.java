package ua.skidchenko.registrationform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.skidchenko.registrationform.entity.enums.CheckStatus;

public interface CheckStatusRepositury extends JpaRepository<CheckStatus,Long> {
}
