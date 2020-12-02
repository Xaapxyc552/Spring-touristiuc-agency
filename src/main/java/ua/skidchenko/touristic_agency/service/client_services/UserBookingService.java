package ua.skidchenko.touristic_agency.service.client_services;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ua.skidchenko.touristic_agency.entity.Check;

public interface UserBookingService {
    @Transactional
    Check bookTourByIdForUsername(Long tourId, String username);

    @Transactional
    Page<Check> findAllChecksByUsernameOrderByStatus(String username, int page);

    @Transactional
    Boolean cancelBookingByCheckId(Long checkId, String username);
}
