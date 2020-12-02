package ua.skidchenko.touristic_agency.service.client_services;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ua.skidchenko.touristic_agency.entity.Check;

public interface ManagerBookingService {
    @Transactional
    Boolean declineBooking(Long checkId);

    Boolean confirmBooking(Long checkId);

    Page<Check> getPagedWaitingChecks(int currentPage);
}
