package ua.skidchenko.registrationform.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.skidchenko.registrationform.dto.TourDTO;
import ua.skidchenko.registrationform.service.TourService;

import javax.validation.Valid;

@Log4j2
@Controller
@RequestMapping("/admin")
public class AdminController {

    final
    TourService tourService;

    public AdminController(TourService tourService) {
        this.tourService = tourService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/new-tour")
    public String createNewTour() {
        return "admin/newTour";
    }

    @PostMapping("/new-tour/create")
    public String createTourFromDTO(@Valid TourDTO tourDTO) {
        log.info(tourDTO.toString());
        tourService.saveNewTour(tourDTO);
        return "redirect:/admin/confirm";
    }

    @PostMapping("/tour/save")
    public String saveTourAfterChanges(@Valid TourDTO tourDTO) {
        log.info(tourDTO.toString());
        tourService.updateTourAfterChanges(tourDTO);
        return "redirect:/admin/confirm";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/tour/edit/{tourId}")
    public String getTourByIdToEdit(Model model,
                                    @PathVariable(name = "tourId") Long tourId) {
        TourDTO tourDTO = tourService.getWaitingTourDTOById(tourId);

        model.addAttribute("tourDTO",tourDTO);
        return "/admin/editTour";
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
}
