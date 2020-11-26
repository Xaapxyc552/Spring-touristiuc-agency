package ua.skidchenko.registrationform.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Log4j2
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/data")
    public String getAdminData(Principal principal,
                               Model model) {
        log.info("Admin page visited" +principal.getName());
        model.addAttribute("currentUsername",principal.getName());
        return "adminPage";
    }
}
