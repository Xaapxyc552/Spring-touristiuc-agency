package ua.skidchenko.registrationform.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.skidchenko.registrationform.entity.Check;
import ua.skidchenko.registrationform.exceptions.NotPresentInDatabaseException;

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

    @ExceptionHandler({IllegalArgumentException.class,
            NotPresentInDatabaseException.class})
    String handle(RuntimeException ex, Model model);
}
