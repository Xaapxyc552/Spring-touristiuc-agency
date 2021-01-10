package ua.skidchenko.touristic_agency.controller;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.touristic_agency.controller.exceptions.WrongFormInputDataException;
import ua.skidchenko.touristic_agency.dto.TourDTO;
import ua.skidchenko.touristic_agency.exceptions.CheckNotPresentInDBException;
import ua.skidchenko.touristic_agency.exceptions.PropertyLocalizedException;
import ua.skidchenko.touristic_agency.exceptions.TourNotPresentInDBException;
import ua.skidchenko.touristic_agency.exceptions.UsernameNotFoundException;
import ua.skidchenko.touristic_agency.service.client_services.AdminTourService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${dollar.course}")
    private Double dollarCourse;

    final private AdminTourService tourService;

    private final MessageSource messageSource;

    public AdminController(AdminTourService tourService, MessageSource messageSource) {
        this.tourService = tourService;
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/new-tour")
    public String createNewTour(Model model) {
        model.addAttribute("dollarCourse", dollarCourse);
        return "admin/newTour";
    }

    @PostMapping("/new-tour/create")
    public String createTourFromDTO(@Valid TourDTO tourDTO,
                                    BindingResult bindingResult,
                                    HttpServletRequest request) {
        checkValidationErrorsElseException(tourDTO, bindingResult);
        log.info("Creating new tour from TourDTO: {}", tourDTO.toString());
        transformMoneyInTourDTO(tourDTO, request);
        tourService.saveNewTour(tourDTO);
        return "redirect:/admin/confirm";
    }


    @PostMapping("/tour/save")
    public String saveTourAfterChanges(@Valid TourDTO tourDTO,
                                       BindingResult bindingResult,
                                       HttpServletRequest request) {
        checkValidationErrorsElseException(tourDTO, bindingResult);
        log.info("Saving tour after editing : {}", tourDTO.toString());
        transformMoneyInTourDTO(tourDTO, request);
        tourService.updateTourAfterChanges(tourDTO);
        return "redirect:/admin/confirm";
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/tour/edit/{tourId}")
    public String getTourByIdToEdit(Model model,
                                    @PathVariable(name = "tourId") Long tourId) {
        log.info("Retrieving tour by tourId from DB to be edited by user. Tour id: {}", tourId);
        TourDTO tourDTO = tourService.getWaitingTourDTOById(tourId);
        model.addAttribute("tourDTO", tourDTO);
        model.addAttribute("dollarCourse", dollarCourse);
        return "/admin/editTour";
    }

    @PostMapping("/tour/delete/{tourId}")
    public String markTourAsDeleted(@PathVariable(name = "tourId") Long tourId) {
        log.info("Marking tour as deleted by tourId. Tour id: {}", tourId);
        tourService.markTourAsDeleted(tourId);
        log.info("Tour marked as deleted. Tour id: " + tourId);
        return "redirect:/tours/list/1";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/confirm")
    public String confirmOperationPage(Model model) {
        log.info("Redirected to PRG-page.");
        model.addAttribute("message", "tour.created");
        model.addAttribute("href", "/admin/new-tour");
        model.addAttribute("hrefDescription", "tour.created.href.description");
        return "singleMessagePage";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({TourNotPresentInDBException.class,
            UsernameNotFoundException.class,
            CheckNotPresentInDBException.class})
    public String handleException(PropertyLocalizedException exception, Model model,
                                  Locale locale) {
        log.warn("Handling exception: {}", exception.getMessage());
        model.addAttribute("error", messageSource.getMessage(
                exception.getPropertyExceptionCode(), null, locale)
        );
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({WrongFormInputDataException.class})
    public String handleValidationExceptions(WrongFormInputDataException exception, Model model) {
        log.warn("Handling exception: {}",exception.getErrors());
        model.addAttribute("errors", exception.getErrors());
        return "validationErrors";
    }

    public void checkValidationErrorsElseException(@Valid TourDTO tourDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Exception during validation of tourDTO {}",tourDTO.toString());
            throw new WrongFormInputDataException("Entered incorrect data.",
                    getValidationErrors(bindingResult));
        }
    }

    @NotNull
    private List<String> getValidationErrors(BindingResult bindingResult) {
        return bindingResult.
                getAllErrors().
                stream().
                map(ObjectError::getDefaultMessage).
                collect(Collectors.toList());
    }

    private void transformMoneyInTourDTO(@Valid TourDTO tourDTO, HttpServletRequest request) {
        Optional<Cookie> lang = Arrays.stream(request.getCookies())
                .filter(n -> n.getName().equals("lang"))
                .findFirst();
        if (!lang.isPresent()) {
            log.warn("Localization cookie is not present in request!");
            throw new IllegalStateException("Localization cookie is not present in request!");
        }
        String langCookie = lang.get().getValue();
        switch (langCookie) {
            case "en-GB":
                tourDTO.setPrice(String.valueOf((int) (Double.parseDouble(tourDTO.getPrice()) * 100 * dollarCourse)));
                break;
            case "uk-UA":
                tourDTO.setPrice(String.valueOf((int) (Double.parseDouble(tourDTO.getPrice()) * 100)));
                break;
            default: {
                log.warn("Language is unsupported!");
                throw new IllegalStateException("Language is unsupported!");
            }
        }
    }
}
