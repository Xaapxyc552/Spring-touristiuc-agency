package ua.skidchenko.touristic_agency.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.skidchenko.touristic_agency.entity.User;
import ua.skidchenko.touristic_agency.service.TourService;
import ua.skidchenko.touristic_agency.service.UserService;
import ua.skidchenko.touristic_agency.service.client_services.UserBookingService;

import javax.servlet.http.Cookie;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserBookingService bookingService;

    @InjectMocks
    private UserController userController;

    @Mock
    private MessageSource messageSource;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
        ReflectionTestUtils.setField(userController, "dollarCourse", 28.5);
    }

    @Test
    void personalAccountPage_CorrectInput_Assert200() throws Exception {
        String username = "username";
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        when(bookingService.findAllChecksByUsernameOrderByStatus(eq(username), anyInt())).thenReturn(Page.empty());
        when(userService.getUserByUsername(username)).thenReturn(new User());
        mvc.perform(MockMvcRequestBuilders.get("/user/personal-account/{page}", 1)
                .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("personalAccount"));
    }

    @Test
    void rechargeUserWallet_CorrectInput_AssertRedirect() throws Exception {
        String username = "username";
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(username);

        Cookie cookie = Mockito.mock(Cookie.class);
        Mockito.when(cookie.getName()).thenReturn("lang");
        Mockito.when(cookie.getValue()).thenReturn("en-GB");


        when(userService.chargeUserWallet(anyLong(),eq(username))).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post("/user/recharge", 1)
                .param("amountOfCharge","1000")
                .cookie(cookie)
                .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/personal-account/1"));
    }
}