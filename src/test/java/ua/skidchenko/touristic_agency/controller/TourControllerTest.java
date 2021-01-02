package ua.skidchenko.touristic_agency.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.skidchenko.touristic_agency.controller.enums.OrderOfTours;
import ua.skidchenko.touristic_agency.entity.Check;
import ua.skidchenko.touristic_agency.entity.enums.TourType;
import ua.skidchenko.touristic_agency.exceptions.ForbiddenOperationException;
import ua.skidchenko.touristic_agency.exceptions.UserHasNotEnoughMoney;
import ua.skidchenko.touristic_agency.service.TourService;
import ua.skidchenko.touristic_agency.service.client_services.AdminTourService;
import ua.skidchenko.touristic_agency.service.client_services.UserBookingService;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TourControllerTest {

    @Mock
    private TourService tourService;

    @Mock
    private UserBookingService bookingService;

    @InjectMocks
    private TourController tourController;

    @Mock
    private MessageSource messageSource;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(tourController)
                .build();
        ReflectionTestUtils.setField(tourController, "dollarCourse", 28.5);
    }

    @Test
    void getTours_WithCorrectInput_Assert200() throws Exception {
        when(tourService.getPagedWaitingToursOrderedByArgs(any(), anyList(), anyString(), anyInt()))
                .thenReturn(Page.empty());

        mvc.perform(MockMvcRequestBuilders.get("/tours/list/{page}", 1)
                .param("selectedTourTypes", TourType.getInstanceByType(TourType.Type.SHOPPING).getType().name())
                .param("direction", "desc")
                .param("order", OrderOfTours.AMOUNT_OF_PERSONS.name()))
                .andExpect(model().attributeExists("tours", "currentPage", "pagesSequence", "dollarCourse"))
                .andExpect(status().isOk())
                .andExpect(view().name("tours"));
    }

    @Test
    void getTours_WithPartialCorrectInput_Assert200() throws Exception {
        when(tourService.getPagedWaitingToursOrderedByArgs(isNull(), isNull(), isNull(), anyInt()))
                .thenReturn(Page.empty());

        mvc.perform(MockMvcRequestBuilders.get("/tours/list/{page}", 1))
                .andExpect(model().attributeExists("tours", "currentPage", "pagesSequence", "dollarCourse"))
                .andExpect(status().isOk())
                .andExpect(view().name("tours"));
    }

    @Test
    void bookTour_EnoughMoney_Assert300() throws Exception {
        when(bookingService.bookTourByIdForUsername(anyLong(), anyString()))
                .thenReturn(null);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        mvc.perform(MockMvcRequestBuilders.post("/tours/book/{page}", 1)
                .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tours/confirmed-operation"));
    }

    @Test
    void bookTour_UserWithoutMoney_Assert400() throws Exception {
        when(bookingService.bookTourByIdForUsername(anyLong(), anyString()))
                .thenThrow(UserHasNotEnoughMoney.class);

        String message = "message";
        when(messageSource.getMessage(anyString(), isNull(), any())).thenReturn(message);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        mvc.perform(MockMvcRequestBuilders.post("/tours/book/{page}", 1)
                .principal(principal))
                .andExpect(model().attribute("error",message))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
    }

    @Test
    void removeBookingFromTour_Assert300() throws Exception {
        when(bookingService.cancelBookingByCheckId(anyLong(), anyString()))
                .thenReturn(null);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        mvc.perform(MockMvcRequestBuilders.post("/tours/remove/{page}", 1)
                .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tours/confirmed-operation"));
    }

    @Test
    void removeBookingFromTour_WrongUser_Assert400() throws Exception {
        when(bookingService.cancelBookingByCheckId(anyLong(), anyString()))
                .thenThrow(ForbiddenOperationException.class);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        String message = "message";
        when(messageSource.getMessage(anyString(), isNull(), any())).thenReturn(message);

        mvc.perform(MockMvcRequestBuilders.post("/tours/remove/{page}", 1)
                .principal(principal))
                .andExpect(model().attribute("error",message))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
    }

}