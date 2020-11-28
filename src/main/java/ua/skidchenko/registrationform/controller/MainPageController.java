package ua.skidchenko.registrationform.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.skidchenko.registrationform.service.TourService;


@Controller
@Log4j2
@RequestMapping("/main")
public class MainPageController {

    final
    TourService tourService;

    public MainPageController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("")
    public String main () {
        return "redirect:/tours/list/1";
    }




}
