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
import ua.skidchenko.touristic_agency.entity.enums.Role;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.exceptions.ForbiddenOperationExceprtion;
import ua.skidchenko.touristic_agency.exceptions.NotPresentInDatabaseException;
import ua.skidchenko.touristic_agency.repository.CheckRepository;
import ua.skidchenko.touristic_agency.repository.TourRepository;
import ua.skidchenko.touristic_agency.repository.UserRepository;
import ua.skidchenko.touristic_agency.service.client_services.UserBookingService;

import java.util.Collections;

@Service
@Log4j2
public class UserBookingServiceImpl implements UserBookingService {

    @Value("${page.size}")
    private int pageSize;

    final
    TourRepository tourRepository;

    final
    UserRepository userRepository;

    final
    CheckRepository checkRepository;

    public UserBookingServiceImpl(TourRepository tourRepository,
                                  UserRepository userRepository,
                                  CheckRepository checkRepository) {
        this.tourRepository = tourRepository;
        this.userRepository = userRepository;
        this.checkRepository = checkRepository;
    }

    @Override
    @Transactional
    public Check bookTourByIdForUsername(Long tourId, String username) {
        log.info("Booking tour for user by username and tourId. " +
                "Username: " + username + ". Tour ID:" + tourId + ".");
        Tour tour = getTourFromRepositoryByIdAndStatus(tourId, TourStatus.WAITING);
        User user = getUserFromRepository(username);

        if (user.getMoney().compareTo(tour.getPrice()) < 0) {
            log.warn("User has not enough money");
            throw new IllegalArgumentException("User has not enough money");
        }
        user.setMoney(user.getMoney() - tour.getPrice());
        tour.setTourStatus(TourStatus.REGISTERED);
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
        log.info("Retrieving paged user's checks ordered by status. Username: " +
                "" + username + ". Page: " + page);
        User byUsername = getUserFromRepository(username);
        PageRequest pr = PageRequest.of(page, pageSize);
        return checkRepository.findAllByUserOrderByStatus(byUsername, pr);
    }

    @Override
    @Transactional
    public Boolean cancelBookingByCheckId(Long checkId, String username) {
        log.info("Canceling booking by checkId. Check ID: " + checkId.toString());
        Check checkFromDB = getCheckFromRepositoryByIdAndStatus(checkId,
                CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM));
        User userFromDB = checkFromDB.getUser();
        if (!userFromDB.getUsername().equals(username)) {
            throw new ForbiddenOperationExceprtion("Username of check's owner not equals to your.");
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

    private Tour getTourFromRepositoryByIdAndStatus(Long tourId, TourStatus status) {
        return tourRepository.findByIdAndTourStatus(tourId, status)
                .orElseThrow(() -> {
                            log.warn("Tour not presented in Database. Tour id: " + tourId);
                            return new NotPresentInDatabaseException(
                                    "Tour not presented in Database. Tour id: " + tourId);
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
