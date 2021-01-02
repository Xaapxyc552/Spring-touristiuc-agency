package ua.skidchenko.touristic_agency.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ua.skidchenko.touristic_agency.dto.UserDTO;
import ua.skidchenko.touristic_agency.entity.User;
import ua.skidchenko.touristic_agency.service.UserService;
import ua.skidchenko.touristic_agency.service.client_services.AdminTourService;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SignUpControllerTest {

    //
    @Mock
    private UserService userService;

    @InjectMocks
    SignUpController signUpController;

//    @MockBean
//    private MessageSource messageSource;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(signUpController)
                .build();
    }


    @Test
    void newUserPage() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("registrationPage"));
    }

    @Test
    void createNewUserWithRightInputAssert200() throws Exception {
        Mockito.when(userService.saveUser(any())).thenReturn(any());
        UserDTO build = UserDTO.builder().password("password")
                .firstname("firstname")
                .email("email@email")
                .username("username")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(build, new TypeReference<Map<String, Object>>() {
        });

        MultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        map.forEach((key, value) -> {
            if (key != null && value != null)
                linkedMultiValueMap.add(key, value.toString());
        });

        mvc.perform(MockMvcRequestBuilders.post("/signup").params(linkedMultiValueMap))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signup/confirm"));
    }

    @Test
    void createNewUserWithWrongInputAssert400() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/signup"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("validationErrors"));
    }

    @Test
    void userSuccessfullyRegistered() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(
                "/signup/confirm"))
                .andExpect(model().attributeExists("message","href","hrefDescription"))
                .andExpect(status().isOk())
                .andExpect(view().name("singleMessagePage"));
    }
}