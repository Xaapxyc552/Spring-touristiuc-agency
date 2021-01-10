package ua.skidchenko.touristic_agency.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.touristic_agency.controller.enums.OrderOfTours;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.entity.enums.TourType;
import ua.skidchenko.touristic_agency.exceptions.*;
import ua.skidchenko.touristic_agency.service.TourService;
import ua.skidchenko.touristic_agency.service.client_services.UserBookingService;
import ua.skidchenko.touristic_agency.service.util.TourSortingHolder;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
@RequestMapping("/tours")
public class TourController {

    private static final String PRIMARY_SORTING_PROPERTY = "burning";


    private static final int START_PAGE_NUM = 1;
    public static final String SORTING_HOLDER = "sortingHolder";
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
    //TODO переделать или отрефакторить
    public String getTours(Model model,
                           @RequestParam(name = "order", required = false) OrderOfTours orderOfTours,
                           @RequestParam(name = "direction", required = false) String direction,
                           @PathVariable(name = "page") int currentPage,
                           @RequestParam(name = "selectedTourTypes", required = false) ArrayList<String> tourTypes,
                           HttpSession session) {
        log.info("Retrieving ordered paged tours with status \"WAITING\" from DB.");
        TourSortingHolder userSortingHolder;

        if (orderOfTours != null && direction != null) {
            TourSortingHolder.TourSortingHolderBuilder userSortingHolderBuilder = TourSortingHolder.builder()
                    .currentPage(0)
                    .tourTypes(TourType.getTourTypesFromStringList(tourTypes))
                    .sorting(Sort.by(new Sort.Order(Sort.Direction.DESC, PRIMARY_SORTING_PROPERTY),
                            new Sort.Order(Sort.Direction.fromString(direction), orderOfTours.getPropertyToSort())));
            if (!tourTypes.isEmpty()) {
                userSortingHolder = userSortingHolderBuilder
                        .tourTypes(TourType.getTourTypesFromStringList(tourTypes))
                        .build();
            } else {
                userSortingHolder = userSortingHolderBuilder.tourTypes(TourType.getEnumMembersAsList())
                        .build();
            }
            session.setAttribute(SORTING_HOLDER, userSortingHolder);
        } else {
            userSortingHolder = getSortFromSessionElseGetDefault(session);
        }
        Page<Tour> orderedToursPage = tourService.getPagedWaitingToursOrderedByArgs(userSortingHolder);
        List<Integer> pagesSequence = IntStream
                .rangeClosed(START_PAGE_NUM, orderedToursPage.getTotalPages())
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("tours", orderedToursPage.getContent())
                .addAttribute("currentPage", currentPage)
                .addAttribute("pagesSequence", pagesSequence)
                .addAttribute("dollarCourse", dollarCourse)
                .addAttribute("availableTourTypes", Arrays.asList(TourType.Type.values()))
                .addAttribute("selectedTourTypes",
                        userSortingHolder.getTourTypes().stream()
                                .map(TourType::getType)
                                .collect(Collectors.toList()))
                .addAttribute("order",
                        orderOfTours == null ?
                                OrderOfTours.HOTEL_TYPE.name() : orderOfTours.name())
                .addAttribute("direction", direction == null?
                Sort.Direction.DESC.name() : direction);
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

    private TourSortingHolder getSortFromSessionElseGetDefault(HttpSession session) {
        log.info("Getting user's sorting from session.");
        Object tourSortingHolder;
        if ((tourSortingHolder = session.getAttribute(SORTING_HOLDER)) != null) {
            return (TourSortingHolder) tourSortingHolder;
        }
        Sort.Order orderByBurning = new Sort.Order(Sort.Direction.DESC, PRIMARY_SORTING_PROPERTY);
        Sort.Order orderByHotelType = new Sort.Order(
                Sort.Direction.DESC, OrderOfTours.HOTEL_TYPE.getPropertyToSort()
        );
        return new TourSortingHolder(0,
                Sort.by(Arrays.asList(orderByBurning, orderByHotelType)),
                TourType.getEnumMembersAsList());

    }

}
