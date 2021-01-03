package ua.skidchenko.touristic_agency.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.touristic_agency.controller.enums.OrderOfTours;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.exceptions.*;
import ua.skidchenko.touristic_agency.service.TourService;
import ua.skidchenko.touristic_agency.service.client_services.UserBookingService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
@RequestMapping("/tours")
public class TourController {

    private static final int START_PAGE_NUM = 1;
    @Value("${dollar.course}")
    private Double dollarCourse;

    final
    TourService tourService;

    final
    UserBookingService bookingService;

    private final MessageSource messageSource;

    public TourController(TourService tourService, UserBookingService bookingService, MessageSource messageSource) {
        this.tourService = tourService;
        this.bookingService = bookingService;
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list/{page}")
    //TODO разделить на несколько методов с зависимостями от pathVariable
    // и добавить principal и передачу username в метод
    public String getTours(Model model,
                           @RequestParam(name = "order", required = false) OrderOfTours order,
                           @RequestParam(name = "direction", required = false) String direction,
                           @PathVariable(name = "page") int currentPage,
                           @RequestParam(name = "selectedTourTypes", required = false) ArrayList<String> tourTypes) {
        log.info("Retrieving ordered paged tours with status \"WAITING\" from DB.");
        Page<Tour> orderedToursPage = tourService.getPagedWaitingToursOrderedByArgs(
                order, tourTypes, direction, currentPage - 1);
        List<Integer> pagesSequence = IntStream
                .rangeClosed(START_PAGE_NUM, orderedToursPage.getTotalPages())
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("tours", orderedToursPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pagesSequence", pagesSequence);
        model.addAttribute("dollarCourse", dollarCourse);
        return "tours";
    }

    @PostMapping("/book/{tourId}")
    public String bookTour(Model model,
                           @PathVariable(name = "tourId") Long tourId,
                           Principal principal) {
        String username = principal.getName();
        log.info("Starting booking tour to user. Username: " + username + " Tour ID: " + tourId);
        bookingService.bookTourByIdForUsername(tourId, username);
        return "redirect:/tours/confirmed-operation";
    }

    @PostMapping("/remove/{checkId}")
    public String removeBookingFromTour(@PathVariable(name = "checkId") Long checkId,
                                        Principal principal) {
        String username = principal.getName();
        log.info("Removing booking from tour by checkId. Username: " + username + " Check ID: " + checkId);
        bookingService.cancelBookingByCheckId(checkId, username);
        return "redirect:/tours/confirmed-operation";
    }

    @GetMapping("/confirmed-operation")
    public String confirmedOperation() {
        return "redirect:/user/personal-account/1";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({PropertyLocalizedException.class})
    public String handleException(Model model,
                                  PropertyLocalizedException ex,
                                  Locale locale) {
        log.warn("Handling exception in TourController. Exception: " + ex.getMessage());
        model.addAttribute("error", messageSource.getMessage(
                ex.getPropertyExceptionCode(), null, locale));
        return "error";
    }

}
