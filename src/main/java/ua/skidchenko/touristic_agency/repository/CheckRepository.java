package ua.skidchenko.touristic_agency.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.skidchenko.touristic_agency.entity.Check;
import ua.skidchenko.touristic_agency.entity.User;
import ua.skidchenko.touristic_agency.entity.enums.CheckStatus;

import java.util.Collection;

public interface CheckRepository extends JpaRepository<Check,Long> {

    <S extends Check> Page<S> findAllByUserOrderByStatus(User user, Pageable pageable);

    Page<Check> findAllByStatus(CheckStatus instanceByEnum, Pageable pageable);
    Page<Check> findAllByStatusIn(Collection<CheckStatus> statuses, Pageable pageable);
}
