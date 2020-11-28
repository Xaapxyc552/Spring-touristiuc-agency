package ua.skidchenko.registrationform.service;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
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
import ua.skidchenko.registrationform.exceptions.NotPresentInDatabaseException;
import ua.skidchenko.registrationform.repository.CheckRepository;
import ua.skidchenko.registrationform.repository.TourRepository;
import ua.skidchenko.registrationform.repository.UserRepository;

import java.util.List;

@Service
@Log4j2
public class BookingService {

    @Value("${page.size}")
    int pageSize;

    final
    TourRepository tourRepository;

    final
    UserRepository userRepository;

    final
    CheckRepository checkRepository;

    public BookingService(TourRepository tourRepository, UserRepository userRepository, CheckRepository checkPerository) {
        this.tourRepository = tourRepository;
        this.userRepository = userRepository;
        this.checkRepository = checkPerository;
    }

    @Transactional
    public Check bookTourByIdForUsername(Long tourId, String username) {
        Tour tour = tourRepository.findByIdAndTourStatus(tourId, TourStatus.WAITING)
                .orElseThrow(() -> {
                            log.warn("Tour not presented in Database. Tour id: " + tourId);
                            return new NotPresentInDatabaseException(
                                    "Tour not presented in Database. Tour id: " + tourId);
                        }
                );
        User user = userRepository.findByUsernameAndRole(username, Role.ROLE_USER)
                .orElseThrow(() -> {
                            log.warn("User not presented in Database. Username: " + tourId);
                            return new NotPresentInDatabaseException(
                                    "User not presented in Database. Username: " + username);
                        }
                );
        if (user.getMoney().compareTo(tour.getPrice()) < 0) {
            log.warn("User has not enough money");
            throw new IllegalArgumentException("User has not enough money");    //TODO
        }
        user.setMoney(user.getMoney() - tour.getPrice());
        userRepository.save(user);
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

    @Transactional
    public List<Check> findAllChecksByUsernameOrderByStatus(String username) {
        User byUsername = userRepository.findByUsername(username).orElseThrow(() -> {
                    log.warn("User not presented in Database. Username: " + username);
                    return new NotPresentInDatabaseException(
                            "User not presented in Database. Username: " + username);
                }
        );
        PageRequest pr = PageRequest.of(0, pageSize); //TODO
        return checkRepository.findAllByUserOrderByStatus(byUsername, pr).getContent();
    }

    @ExceptionHandler({IllegalArgumentException.class,
            NotPresentInDatabaseException.class})
    public String handle(RuntimeException ex, Model model) {
        model.addAttribute("message", ex.getLocalizedMessage());
        return "singleMessagePage";
    }

}
