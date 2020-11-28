package ua.skidchenko.registrationform.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.registrationform.entity.Tour;
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

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list/{page}")
    public String getTours(Model model,
                           @RequestParam(name = "order", required = false) OrderOfTours order,
                           @RequestParam(name = "direction", required = false) String direction,
                           @PathVariable(name = "page") int page) {
        List<Tour> orderedTours;
        int totalPages;

        page = (page == 0) ? 1 : page;
        Page<Tour> orderedToursPage = tourService.getPagedWaitingToursOrderedByArgs(
                order, direction, page - 1
        );
        totalPages = orderedToursPage.getTotalPages();
        orderedTours = orderedToursPage.getContent();
//        model.addAttribute("currentOrder", order.name());
//        model.addAttribute("currentDirection", direction);
        List<Integer> pagesSequence = IntStream
                .rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("tours", orderedTours);
        model.addAttribute("currentPage", page);
        model.addAttribute("pagesSequence", pagesSequence);
        return "tours";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/book/{tourName}")
    public String bookTour(Model model,
                           @PathVariable(name = "tourName") String tourName,
                           Principal principal) {

        return "tours";
    }
}
