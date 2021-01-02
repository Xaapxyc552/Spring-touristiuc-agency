package ua.skidchenko.touristic_agency.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.skidchenko.touristic_agency.entity.Check;
import ua.skidchenko.touristic_agency.entity.User;
import ua.skidchenko.touristic_agency.entity.enums.CheckStatus;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CheckRepository extends JpaRepository<Check,Long> {

    <S extends Check> Page<S> findAllByUserOrderByStatus(User user, Pageable pageable);

    Page<Check> findAllByStatusIn(Collection<CheckStatus> statuses, Pageable pageable);

    Optional<Check> findByIdAndStatusIn(Long id, Collection<CheckStatus> statuses);

}
