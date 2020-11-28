package ua.skidchenko.registrationform.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.registrationform.entity.Tour;
import ua.skidchenko.registrationform.exceptions.NotPresentInDatabaseException;
import ua.skidchenko.registrationform.service.BookingService;
import ua.skidchenko.registrationform.service.TourService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
@RequestMapping("/tours")
public class TourController {

    final
    TourService tourService;

    final
    BookingService bookingService;

    public TourController(TourService tourService, BookingService bookingService) {
        this.tourService = tourService;
        this.bookingService = bookingService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list/{page}")
    public String getTours(Model model,
                           @RequestParam(name = "order", required = false) OrderOfTours order,
                           @RequestParam(name = "direction", required = false) String direction,
                           @PathVariable(name = "page") int page) {
        page = (page == 0) ? 1 : page;
        Page<Tour> orderedToursPage = tourService.getPagedWaitingToursOrderedByArgs(
                order, direction, page - 1
        );
//        model.addAttribute("currentOrder", order.name());
//        model.addAttribute("currentDirection", direction);
        List<Integer> pagesSequence = IntStream
                .rangeClosed(1,  orderedToursPage.getTotalPages())
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("tours", orderedToursPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pagesSequence", pagesSequence);
        return "tours";
    }

    @GetMapping("/book/{tourId}")
    public String bookTour(Model model,
                           @PathVariable(name = "tourId") Long tourId,
                           Principal principal) {
        String username = principal.getName();
        log.info("Starting booking tour to user. " +
                "Username: " + username + " Tour ID: " + tourId);
        bookingService.bookTourByIdForUsername(tourId, username);
        return "redirect:/user/personal-account";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class,
            NotPresentInDatabaseException.class})
    public String handleException(Model model, RuntimeException ex) {
        model.addAttribute("cause",ex.getCause());
        model.addAttribute("message",ex.getLocalizedMessage());
        log.info("                         "+ex.getLocalizedMessage());
        return "singleMessagePage";
    }
}
