package ua.skidchenko.registrationform.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.skidchenko.registrationform.entity.Tour;
import ua.skidchenko.registrationform.entity.enums.TourStatus;

import java.util.Collection;
import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long> {

    Page<Tour> findAllByTourStatus(TourStatus tourStatus,Pageable pageable);

    Optional<Tour> findByIdAndTourStatus(Long aLong, TourStatus status);
    Optional<Tour> findByIdAndTourStatusIn(Long id, Collection<TourStatus> statuses);

    @NotNull
    @Override
    <S extends Tour> Page<S> findAll(@NotNull Example<S> example, @NotNull Pageable pageable);

}
