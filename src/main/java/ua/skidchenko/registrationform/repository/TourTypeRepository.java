package ua.skidchenko.registrationform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.skidchenko.registrationform.entity.enums.TourType;

public interface TourTypeRepository extends JpaRepository<TourType,Long> {
}
