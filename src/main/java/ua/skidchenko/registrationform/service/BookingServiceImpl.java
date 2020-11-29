package ua.skidchenko.registrationform.service;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.skidchenko.registrationform.entity.Check;
import ua.skidchenko.registrationform.entity.Tour;
import ua.skidchenko.registrationform.entity.User;
import ua.skidchenko.registrationform.entity.enums.CheckStatus;
import ua.skidchenko.registrationform.entity.enums.Role;
import ua.skidchenko.registrationform.entity.enums.TourStatus;
import ua.skidchenko.registrationform.exceptions.ForbiddenOperationExceprtion;
import ua.skidchenko.registrationform.exceptions.NotPresentInDatabaseException;
import ua.skidchenko.registrationform.repository.CheckRepository;
import ua.skidchenko.registrationform.repository.TourRepository;
import ua.skidchenko.registrationform.repository.UserRepository;

import java.util.Collections;

@Service
@Log4j2
public class BookingServiceImpl implements BookingService {

    @Value("${page.size}")
    int pageSize;

    final
    TourRepository tourRepository;

    final
    UserRepository userRepository;

    final
    CheckRepository checkRepository;

    public BookingServiceImpl(TourRepository tourRepository, UserRepository userRepository, CheckRepository checkPerository) {
        this.tourRepository = tourRepository;
        this.userRepository = userRepository;
        this.checkRepository = checkPerository;
    }

    @Override
    @Transactional
    public Check bookTourByIdForUsername(Long tourId, String username) {
        Tour tour = getTourFromRepositoryByIdAndStatus(tourId, TourStatus.WAITING);
        User user = getUserFromRepository(username);

        if (user.getMoney().compareTo(tour.getPrice()) < 0) {
            log.warn("User has not enough money");
            throw new IllegalArgumentException("User has not enough money");
        }
        user.setMoney(user.getMoney() - tour.getPrice());
        tour.setTourStatus(TourStatus.REGISTERED);
        tourRepository.save(tour);
        Check bookingCheck = Check.builder()
                .status(
                        CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM)
                )
                .tour(tour)
                .totalPrice(tour.getPrice())
                .user(user)
                .build();
        log.info("Finished creation check:" + bookingCheck.toString());
        return checkRepository.save(bookingCheck);
    }

    @Override
    @Transactional
    public Page<Check> findAllChecksByUsernameOrderByStatus(String username, int page) {
        User byUsername = getUserFromRepository(username);
        PageRequest pr = PageRequest.of(page, pageSize);
        return checkRepository.findAllByUserOrderByStatus(byUsername, pr);
    }

    @Override
    @Transactional
    public Boolean cancelBookingByCheckId(Long checkId, String username) {
        Check checkFromDB = getCheckFromRepository(checkId);
        User userFromDB = checkFromDB.getUser();
        if (!userFromDB.getUsername().equals(username)) {
            throw new ForbiddenOperationExceprtion("Username of check's owner not equals to user's.");
        }
        checkFromDB.getTour().setTourStatus(TourStatus.WAITING);
        userFromDB.setMoney(
                userFromDB.getMoney() + checkFromDB.getTotalPrice()
        );
        checkFromDB.setStatus(CheckStatus.getInstanceByEnum(
                CheckStatus.Status.CANCELED)
        );
        checkRepository.save(checkFromDB);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean declineBooking(Long checkId) {
        Check checkToDecline = getCheckFromRepository(checkId);
        User user = checkToDecline.getUser();
        Tour tourToDecline = checkToDecline.getTour();

        user.setMoney(user.getMoney() + checkToDecline.getTotalPrice());
        tourToDecline.setTourStatus(TourStatus.WAITING);
        checkToDecline.setStatus(CheckStatus.getInstanceByEnum(CheckStatus.Status.DECLINED));
        checkRepository.save(checkToDecline);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean confirmBooking(Long checkId) {
        Check checkToConfirm = getCheckFromRepository(checkId);
        Tour tourToConfirm = checkToConfirm.getTour();

        tourToConfirm.setTourStatus(TourStatus.SOLD);
        checkToConfirm.setStatus(CheckStatus.getInstanceByEnum(CheckStatus.Status.CONFIRMED));
        checkRepository.save(checkToConfirm);
        return Boolean.TRUE;
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

    private Tour getTourFromRepositoryByIdAndStatus(Long tourId, TourStatus status) {
        return tourRepository.findByIdAndTourStatus(tourId, status)
                .orElseThrow(() -> {
                            log.warn("Tour not presented in Database. Tour id: " + tourId);
                            return new NotPresentInDatabaseException(
                                    "Tour not presented in Database. Tour id: " + tourId);
                        }
                );
    }

    private Check getCheckFromRepository(Long checkId) {
        return checkRepository.findById(checkId).orElseThrow(() -> {
                    log.warn("Check not presented in Database. Check ID: " + checkId);
                    return new NotPresentInDatabaseException(
                            "Check not presented in Database. Check ID: " + checkId);
                }
        );
    }

    private User getUserFromRepository(String username) {
        return userRepository.findByUsernameAndRole(username, Role.ROLE_USER)
                .orElseThrow(() -> {
                            log.warn("User not presented in Database. Username: " + username);
                            return new NotPresentInDatabaseException(
                                    "User not presented in Database. Username: " + username);
                        }
                );
    }

    @Override
    @ExceptionHandler({IllegalArgumentException.class,
            NotPresentInDatabaseException.class})
    public String handle(RuntimeException ex, Model model) {
        model.addAttribute("message", ex.getLocalizedMessage());
        return "singleMessagePage";
    }
}
