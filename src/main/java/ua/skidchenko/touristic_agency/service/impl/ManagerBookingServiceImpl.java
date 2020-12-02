package ua.skidchenko.touristic_agency.service.impl;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.skidchenko.touristic_agency.entity.Check;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.entity.User;
import ua.skidchenko.touristic_agency.entity.enums.CheckStatus;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.exceptions.NotPresentInDatabaseException;
import ua.skidchenko.touristic_agency.repository.CheckRepository;
import ua.skidchenko.touristic_agency.repository.TourRepository;
import ua.skidchenko.touristic_agency.repository.UserRepository;
import ua.skidchenko.touristic_agency.service.client_services.ManagerBookingService;

import java.util.Collections;

@Service
@Log4j2
public class ManagerBookingServiceImpl implements ManagerBookingService {

    @Value("${page.size}")
    private int pageSize;

    final
    TourRepository tourRepository;

    final
    UserRepository userRepository;

    final
    CheckRepository checkRepository;

    public ManagerBookingServiceImpl(TourRepository tourRepository,
                                     UserRepository userRepository,
                                     CheckRepository checkRepository) {
        this.tourRepository = tourRepository;
        this.userRepository = userRepository;
        this.checkRepository = checkRepository;
    }

    @Override
    public Page<Check> getPagedWaitingChecks(int currentPage) {
        log.info("Starting retrieving waiting checks from DB.");
        PageRequest pr = PageRequest.of(currentPage, pageSize);
        CheckStatus instanceByEnum = CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM);
        Page<Check> allByStatus = checkRepository.findAllByStatusIn(
                Collections.singletonList(instanceByEnum), pr
        );
        log.info("Checks from DB: " + allByStatus.getContent());
        return allByStatus;
    }

    @Override
    @Transactional
    public Boolean declineBooking(Long checkId) {
        log.info("Canceling booking by checkId. Check ID: " + checkId.toString());
        Check checkToDecline = getCheckFromRepositoryByIdAndStatus(checkId,
                CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM));
        User checkOwner = checkToDecline.getUser();
        Tour tourToDecline = checkToDecline.getTour();

        checkOwner.setMoney(checkOwner.getMoney() + checkToDecline.getTotalPrice());
        tourToDecline.setTourStatus(TourStatus.WAITING);
        checkToDecline.setStatus(CheckStatus.getInstanceByEnum(CheckStatus.Status.DECLINED));
        checkRepository.save(checkToDecline);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean confirmBooking(Long checkId) {
        Check checkToConfirm = getCheckFromRepositoryByIdAndStatus(checkId,
                CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM));
        Tour tourToConfirm = checkToConfirm.getTour();

        tourToConfirm.setTourStatus(TourStatus.SOLD);
        checkToConfirm.setStatus(CheckStatus.getInstanceByEnum(CheckStatus.Status.CONFIRMED));
        checkRepository.save(checkToConfirm);
        return Boolean.TRUE;
    }

    private Check getCheckFromRepositoryByIdAndStatus(Long checkId, CheckStatus tourStatus) {
        return checkRepository.findByIdAndStatusIn(checkId, Collections.singletonList(tourStatus))
                .orElseThrow(() -> {
                            log.warn("Check not presented in Database. Check ID: " + checkId);
                            return new NotPresentInDatabaseException(
                                    "Check not presented in Database. Check ID: " + checkId);
                        }
                );
    }

}