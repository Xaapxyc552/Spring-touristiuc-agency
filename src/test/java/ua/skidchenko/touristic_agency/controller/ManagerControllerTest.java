package ua.skidchenko.touristic_agency.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.skidchenko.touristic_agency.service.client_services.ManagerBookingService;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class ManagerControllerTest {

    @Mock
    private ManagerBookingService bookingService;

    @InjectMocks
    ManagerController managerController;

//    @MockBean
//    private MessageSource messageSource;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(managerController)
                .build();
    }

    @Test
    void getManageableToursAssert200AndModelAttributes() throws Exception {
        Mockito.when(bookingService.getPagedWaitingChecks(anyInt())).thenReturn(Page.empty());
        mvc.perform(MockMvcRequestBuilders.get(
                "/manager/tours-operations/{page}", anyInt()))
                .andExpect(model().attributeExists(
                        "currentPage","waitingChecks","pagesSequence"))
                .andExpect(view().name("manager/managerTourPage"))
                .andExpect(status().isOk());
    }

    @Test
    void declineBookingAssertRedirectToPRGPage() throws Exception {
        Mockito.when(bookingService.declineBooking(anyLong())).thenReturn(Boolean.TRUE);
        mvc.perform(MockMvcRequestBuilders.post(
                "/manager/check/decline/{checkId}", anyInt()))
                .andExpect(flash().attribute("message","response.message.book_declined"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/confirmed-operation"));
    }

    @Test
    void confirmBooking() throws Exception {
        Mockito.when(bookingService.confirmBooking(anyLong())).thenReturn(Boolean.TRUE);
        mvc.perform(MockMvcRequestBuilders.post(
                "/manager/check/confirm/{checkId}", anyInt()))
                .andExpect(flash().attributeExists("message"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manager/confirmed-operation"));
    }

    @Test
    void confirmOperationPage() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(
                "/manager/confirmed-operation"))
                .andExpect(model().attributeExists("message","href","hrefDescription"))
                .andExpect(status().isOk())
                .andExpect(view().name("singleMessagePage"));
    }
}