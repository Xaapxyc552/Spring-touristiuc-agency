package ua.skidchenko.touristic_agency.service.impl;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
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
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.exceptions.CheckNotPresentInDBException;
import ua.skidchenko.touristic_agency.repository.CheckRepository;
import ua.skidchenko.touristic_agency.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerBookingServiceImplTest {

    //
    @Mock
    private UserRepository userRepository;

    @Mock
    private CheckRepository checkRepository;

    @InjectMocks
    ManagerBookingServiceImpl managerBookingService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(managerBookingService, "pageSize", 5);
    }


    @Test
    void getPagedWaitingChecks() {
        when(checkRepository.findAllByStatusIn(anyCollection(), any(Pageable.class))).thenReturn(Page.empty());

        Page<Check> pagedWaitingChecks = managerBookingService.getPagedWaitingChecks(1);
        Assert.assertEquals(pagedWaitingChecks, Page.empty());
    }

    @Test
    void declineBooking_CorrectInputAndCheckExists_AssertMultipleTrue() {
        Long userMoney = 150L;
        Long checkPrice = 150L;

        User user = User.builder()
                .money(userMoney).build();

        Check check = Check.builder()
                .tour(new Tour())
                .user(user)
                .totalPrice(checkPrice)
                .build();

        when(checkRepository.findByIdAndStatusIn(anyLong(), anyCollection())).thenReturn(Optional.of(check));
        when(checkRepository.save(any(Check.class))).thenReturn(new Check());
        Assert.assertTrue(managerBookingService.declineBooking(1L));
        Assert.assertEquals(userMoney + checkPrice, (long) check.getUser().getMoney());
        Assert.assertEquals(TourStatus.WAITING, check.getTour().getTourStatus());
        Assert.assertEquals(CheckStatus.getInstanceByEnum(CheckStatus.Status.DECLINED), check.getStatus());

    }

    @Test
    void declineBooking_CheckDoesntExists_AssertException() {
        when(checkRepository.findByIdAndStatusIn(anyLong(), anyCollection()))
                .thenThrow(CheckNotPresentInDBException.class);

        Assertions.assertThrows(CheckNotPresentInDBException.class,
                () -> managerBookingService.declineBooking(1L));
    }

    @Test
    void confirmBooking_CorrectInputAnCheckExists_AssertMultipleTrue() {
        Tour tour = Tour.builder().build();
        Check check = Check.builder()
                .tour(tour)
                .build();

        when(checkRepository.findByIdAndStatusIn(anyLong(), anyCollection())).thenReturn(Optional.of(check));
        when(checkRepository.save(any(Check.class))).thenReturn(new Check());

        Assert.assertTrue(managerBookingService.confirmBooking(1L));
        Assert.assertEquals(CheckStatus.getInstanceByEnum(CheckStatus.Status.CONFIRMED), check.getStatus());
        Assert.assertEquals(TourStatus.SOLD, check.getTour().getTourStatus());
    }

    @Test
    void confirmBooking_CheckDoesntExists_AssertException() {
        when(checkRepository.findByIdAndStatusIn(anyLong(), anyCollection()))
                .thenThrow(CheckNotPresentInDBException.class);

        Assertions.assertThrows(CheckNotPresentInDBException.class,
                () -> managerBookingService.confirmBooking(1L));
    }
}