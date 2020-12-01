package ua.skidchenko.registrationform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.skidchenko.registrationform.entity.Check;
import ua.skidchenko.registrationform.entity.User;
import ua.skidchenko.registrationform.entity.enums.CheckStatus;

import java.util.Collection;

public interface CheckRepository extends JpaRepository<Check,Long> {

    <S extends Check> Page<S> findAllByUserOrderByStatus(User user, Pageable pageable);

    Page<Check> findAllByStatus(CheckStatus instanceByEnum, Pageable pageable);
    Page<Check> findAllByStatusIn(Collection<CheckStatus> statuses, Pageable pageable);
}
