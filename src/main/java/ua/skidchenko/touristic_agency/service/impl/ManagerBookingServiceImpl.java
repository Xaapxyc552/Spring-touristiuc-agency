package ua.skidchenko.touristic_agency.service.impl;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.skidchenko.touristic_agency.entity.Check;
import ua.skidchenko.touristic_agency.entity.enums.CheckStatus;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.exceptions.CheckNotPresentInDBException;
import ua.skidchenko.touristic_agency.repository.CheckRepository;
import ua.skidchenko.touristic_agency.repository.UserRepository;
import ua.skidchenko.touristic_agency.service.client_services.ManagerBookingService;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@Log4j2
public class ManagerBookingServiceImpl implements ManagerBookingService {

    @Value("${page.size}")
    private int pageSize;

    final
    UserRepository userRepository;

    final
    CheckRepository checkRepository;

    public ManagerBookingServiceImpl(UserRepository userRepository,
                                     CheckRepository checkRepository) {
        this.userRepository = userRepository;
        this.checkRepository = checkRepository;
    }

    @Override
    public Page<Check> getPagedWaitingChecks(int currentPage) {
        log.info("Starting retrieving waiting checks from DB.");
        Page<Check> allByStatus = checkRepository.findAllByStatusIn(
                Collections.singletonList(CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM)),
                PageRequest.of(currentPage, pageSize));
        log.info("Checks from DB: " + allByStatus.getContent());
        return allByStatus;
    }

    @Override
    @Transactional
    public Boolean declineBooking(Long checkId) {
        log.info("Canceling booking by checkId. Check ID: {}", checkId.toString());
        Check checkToDecline = getCheckFromRepositoryByIdAndStatus(checkId,
                CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM));
        checkToDecline.getUser().setMoney(checkToDecline.getUser().getMoney() + checkToDecline.getTotalPrice());
        checkToDecline.getTour().setTourStatus(TourStatus.WAITING);
        checkToDecline.setStatus(CheckStatus.getInstanceByEnum(CheckStatus.Status.DECLINED));
        checkToDecline.setModifiedTime(LocalDateTime.now());
        checkRepository.save(checkToDecline);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean confirmBooking(Long checkId) {
        Check checkToConfirm = getCheckFromRepositoryByIdAndStatus(checkId,
                CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM));

        checkToConfirm.getTour().setTourStatus(TourStatus.SOLD);
        checkToConfirm.setStatus(CheckStatus.getInstanceByEnum(CheckStatus.Status.CONFIRMED));
        checkToConfirm.setModifiedTime(LocalDateTime.now());
        checkRepository.save(checkToConfirm);
        return Boolean.TRUE;
    }

    private Check getCheckFromRepositoryByIdAndStatus(Long checkId, CheckStatus tourStatus) {
        return checkRepository.findByIdAndStatusIn(checkId, Collections.singletonList(tourStatus))
                .orElseThrow(() -> {
                            log.warn("Check not presented in Database. Check ID: {}", checkId);
                            return new CheckNotPresentInDBException(
                                    "Check not presented in Database. Check ID: " + checkId);
                        }
                );
    }

}
