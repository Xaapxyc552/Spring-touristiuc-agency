package ua.skidchenko.touristic_agency.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ua.skidchenko.touristic_agency.entity.Check;

public interface BookingService {
    @Transactional
    Check bookTourByIdForUsername(Long tourId, String username);

    @Transactional
    Page<Check> findAllChecksByUsernameOrderByStatus(String username, int page);

    @Transactional
    Boolean cancelBookingByCheckId(Long checkId, String username);

    @Transactional
    Boolean declineBooking(Long checkId);

    Boolean confirmBooking(Long checkId);

    Page<Check> getPagedWaitingChecks(int currentPage);

}
