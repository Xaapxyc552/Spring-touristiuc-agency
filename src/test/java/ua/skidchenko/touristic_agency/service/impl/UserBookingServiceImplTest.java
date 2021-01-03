package ua.skidchenko.touristic_agency.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import ua.skidchenko.touristic_agency.entity.Check;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.entity.User;
import ua.skidchenko.touristic_agency.entity.enums.CheckStatus;
import ua.skidchenko.touristic_agency.entity.enums.Role;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.exceptions.TourNotPresentInDBException;
import ua.skidchenko.touristic_agency.exceptions.UserHasNotEnoughMoney;
import ua.skidchenko.touristic_agency.exceptions.UsernameNotFoundException;
import ua.skidchenko.touristic_agency.repository.CheckRepository;
import ua.skidchenko.touristic_agency.repository.TourRepository;
import ua.skidchenko.touristic_agency.repository.UserRepository;
import ua.skidchenko.touristic_agency.service.util.TourSortingHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserBookingServiceImplTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CheckRepository checkRepository;

    @InjectMocks
    UserBookingServiceImpl userBookingService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(userBookingService, "pageSize", 5);
    }


    @Test
    void bookTourByIdForUsername_CorrectInput_AssertOk() {
        String username = "username";
        long tourId = 1L;
        long tourPrice = 500L;
        long userMoney = 1000L;
        User userFromRepo = User.builder()
                .id(1L)
                .username(username)
                .money(userMoney)
                .build();

        Tour tourFromRepo = Tour.builder()
                .price(tourPrice)
                .id(tourId)
                .tourStatus(TourStatus.WAITING)
                .build();

        Check checkFromRepo = Check.builder()
                .totalPrice(tourFromRepo.getPrice())
                .id(1L)
                .user(userFromRepo)
                .tour(tourFromRepo)
                .creationTime(LocalDateTime.now())
                .status(CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM))
                .build();

        when(userRepository.findByUsernameAndRole(anyString(), eq(Role.ROLE_USER)))
                .thenReturn(Optional.ofNullable(userFromRepo));
        when(tourRepository.findByIdAndTourStatus(anyLong(), eq(TourStatus.WAITING)))
                .thenReturn(Optional.ofNullable(tourFromRepo));
        when(checkRepository.save(any(Check.class)))
                .thenReturn(checkFromRepo);

        Check check = userBookingService.bookTourByIdForUsername(tourId, username);
        Assert.assertEquals(tourFromRepo.getPrice(), check.getTotalPrice());
        Assert.assertEquals(TourStatus.REGISTERED, tourFromRepo.getTourStatus());
        Assert.assertEquals(userMoney - tourPrice, (long) userFromRepo.getMoney());
        Assert.assertEquals(CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM),
                check.getStatus());
        Assert.assertNotNull(checkFromRepo.getCreationTime());
    }

    @Test
    void bookTourByIdForUsername_NotEnoughMoney_AssertException() {
        String username = "username";
        long tourId = 1L;
        long tourPrice = 1000L;
        long userMoney = 500L;
        User userFromRepo = User.builder()
                .id(1L)
                .username(username)
                .money(userMoney)
                .build();

        Tour tourFromRepo = Tour.builder()
                .price(tourPrice)
                .id(tourId)
                .tourStatus(TourStatus.WAITING)
                .build();

        when(userRepository.findByUsernameAndRole(anyString(), eq(Role.ROLE_USER)))
                .thenReturn(Optional.ofNullable(userFromRepo));
        when(tourRepository.findByIdAndTourStatus(anyLong(), eq(TourStatus.WAITING)))
                .thenReturn(Optional.ofNullable(tourFromRepo));

        Assertions.assertThatExceptionOfType(UserHasNotEnoughMoney.class).isThrownBy(
                () -> userBookingService.bookTourByIdForUsername(tourId, username));
    }

    @Test
    void bookTourByIdForUsername_NoTourInDatabase_AssertException() {
        String username = "username";
        long tourId = 1L;

        when(tourRepository.findByIdAndTourStatus(anyLong(), eq(TourStatus.WAITING)))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(TourNotPresentInDBException.class).isThrownBy(
                () -> userBookingService.bookTourByIdForUsername(tourId, username));
    }

    @Test
    void bookTourByIdForUsername_NoUserInDatabase_AssertException() {
        String username = "username";
        long tourId = 1L;
        Tour tourFromRepo = Tour.builder()
                .id(tourId)
                .tourStatus(TourStatus.WAITING)
                .build();

        when(tourRepository.findByIdAndTourStatus(anyLong(), eq(TourStatus.WAITING)))
                .thenReturn(Optional.of(tourFromRepo));
        when(userRepository.findByUsernameAndRole(anyString(), eq(Role.ROLE_USER)))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(
                () -> userBookingService.bookTourByIdForUsername(tourId, username));
    }

    @Test
    void findAllChecksByUsernameOrderByStatus_CorrectInput_AssertOk() {
        String username = "username";
        int page = 0;
        when(userRepository.findByUsernameAndRole(anyString(),eq(Role.ROLE_USER)))
                .thenReturn(Optional.of(new User()));
        when(checkRepository.findAllByUserOrderByStatus(any(User.class),any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<Check> checks = userBookingService.findAllChecksByUsernameOrderByStatus(username, page);
        Assert.assertNotNull(checks);
    }

    @Test
    void findAllChecksByUsernameOrderByStatus_UserNotExist_AssertException() {
        String username = "username";
        int page = 0;
        when(userRepository.findByUsernameAndRole(anyString(), eq(Role.ROLE_USER)))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(
                () -> userBookingService.findAllChecksByUsernameOrderByStatus(username, page));
    }

    @Test
    void cancelBookingByCheckId() {
        String username = "username";
        long tourId = 1L;
        long tourPrice = 500L;
        long userMoney = 1000L;
        User userFromRepo = User.builder()
                .id(1L)
                .username(username)
                .money(userMoney)
                .build();

        Tour tourFromRepo = Tour.builder()
                .price(tourPrice)
                .id(tourId)
                .tourStatus(TourStatus.WAITING)
                .build();

        Check checkFromRepo = Check.builder()
                .totalPrice(tourFromRepo.getPrice())
                .id(1L)
                .user(userFromRepo)
                .tour(tourFromRepo)
                .status(CheckStatus.getInstanceByEnum(CheckStatus.Status.WAITING_FOR_CONFIRM))
                .build();

        when(checkRepository.findByIdAndStatusIn(anyLong()
                , eq(Collections.singletonList(CheckStatus.getInstanceByEnum(
                        CheckStatus.Status.WAITING_FOR_CONFIRM)))))
                .thenReturn(Optional.of(checkFromRepo));
        when(checkRepository.save(any(Check.class)))
                .thenReturn(checkFromRepo);

        Assert.assertTrue(userBookingService.cancelBookingByCheckId(tourId, username));
        Assert.assertEquals(TourStatus.WAITING, tourFromRepo.getTourStatus());
        Assert.assertEquals(userMoney + tourPrice, (long) userFromRepo.getMoney());
        Assert.assertEquals(CheckStatus.getInstanceByEnum(CheckStatus.Status.CANCELED),
                checkFromRepo.getStatus());
        Assert.assertNotNull(checkFromRepo.getModifiedTime());

    }
}