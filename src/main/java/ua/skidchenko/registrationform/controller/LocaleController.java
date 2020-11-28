package ua.skidchenko.registrationform.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/locale")
@Controller
@Log4j2
public class LocaleController {

    @GetMapping("/")
    public String changeLanguage(@CookieValue("lang") String langCode,
                                 HttpServletRequest request){
        log.info("Language changed to: " + langCode);
        return "redirect:" + request.getHeader("referer");
    }
}
