package ua.skidchenko.touristic_agency.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.entity.enums.TourType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long> {

    Page<Tour> findDistinctByTourTypesInAndTourStatus(Pageable pageable,
                                                   Collection<TourType> tourTypes,
                                                   TourStatus tourStatus);

    Optional<Tour> findByIdAndTourStatus(Long id, TourStatus status);

    Optional<Tour> findByIdAndTourStatusIn(Long id, Collection<TourStatus> statuses);
}
