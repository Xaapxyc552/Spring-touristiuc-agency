package ua.skidchenko.touristic_agency.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.skidchenko.touristic_agency.dto.TourDTO;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.entity.enums.HotelType;
import ua.skidchenko.touristic_agency.entity.enums.TourType;
import ua.skidchenko.touristic_agency.exceptions.TourNotPresentInDBException;
import ua.skidchenko.touristic_agency.service.client_services.AdminTourService;

import javax.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    //
    @Mock
    private AdminTourService tourService;

    Validator validator;

    @InjectMocks
    AdminController adminController;


    @Mock
    private MessageSource messageSource;


    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        Validator validator = Mockito.mock(Validator.class);
        this.validator = validator;
        mvc = MockMvcBuilders.standaloneSetup(adminController)
                .setValidator(validator)
                .build();
        ReflectionTestUtils.setField(adminController, "dollarCourse", 28.5);

    }


    @Test
    void getTourByIdToEdit_CorrectId_Assert200() throws Exception {
        TourDTO tourDTO = new TourDTO();
        Mockito.when(tourService.getWaitingTourDTOById(anyLong())).thenReturn(tourDTO);

        mvc.perform(MockMvcRequestBuilders.get("/admin/tour/edit/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/editTour"))
                .andExpect(model().attribute("tourDTO", tourDTO));
    }

    @Test
    void createNewTour_Assert200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/new-tour"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/newTour"));
    }

    @Test
    void createTour_WithEmptyTourDTO_Assert400() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(adminController)
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/admin/new-tour/create"))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("errors"));

    }

    @Test
    void createTour_ByNonEmptyTourDTO_Assert400() throws Exception {
        Mockito.when(validator.supports(any(Class.class))).thenReturn(true);
        doNothing().when(validator).validate(any(), any(Errors.class));

        Cookie cookie = Mockito.mock(Cookie.class);
        Mockito.when(cookie.getName()).thenReturn("lang");
        Mockito.when(cookie.getValue()).thenReturn("en-GB");

        mvc.perform(MockMvcRequestBuilders.post("/admin/new-tour/create")
                .cookie(cookie)
                .param("price", "10000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/confirm"));
    }

    @Test
    void saveTourAfterChanges_WithoutTourDTO_Assert400() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(adminController)
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/admin/tour/save"))
                .andExpect(view().name("validationErrors"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void markTourAsDeleted_CorrectTourID_AssertRedirect() throws Exception {
        Mockito.when(tourService.markTourAsDeleted(anyLong())).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post("/admin/tour/delete/{id}", anyLong()))
                .andExpect(redirectedUrl("/tours/list/1"));
    }
    @Test
    void markTourAsDeleted_IncorrectTourID_Assert400() throws Exception {
        Mockito.when(tourService.markTourAsDeleted(anyLong())).thenThrow(TourNotPresentInDBException.class);

        String message = "message";
        when(messageSource.getMessage(anyString(), isNull(), any())).thenReturn(message);

        mvc.perform(MockMvcRequestBuilders.post("/admin/tour/delete/{id}", anyLong()))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void markTourAsDeletedAssertException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/admin/tour/delete/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void confirmOperationPageAssert200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/confirm/"))
                .andExpect(view().name("singleMessagePage"))
                .andExpect(model().attributeExists("message", "href", "hrefDescription"))
                .andExpect(status().isOk());
    }

}