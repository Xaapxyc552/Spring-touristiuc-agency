package ua.skidchenko.touristic_agency.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ua.skidchenko.touristic_agency.controller.util.PagingSequenceCreator;
import ua.skidchenko.touristic_agency.entity.Check;
import ua.skidchenko.touristic_agency.exceptions.NotPresentInDatabaseException;
import ua.skidchenko.touristic_agency.exceptions.PropertyLocalizedException;
import ua.skidchenko.touristic_agency.service.client_services.ManagerBookingService;

import java.util.List;
import java.util.Locale;

@Log4j2
@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Value("${dollar.course}")
    private Double dollarCourse;

    private static final String ATTRIBUTE_TO_PASS_IF_CONFIRMED = "message";

    private final
    ManagerBookingService bookingService;

    private final MessageSource messageSource;

    public ManagerController(ManagerBookingService bookingService, MessageSource messageSource) {
        this.bookingService = bookingService;
        this.messageSource = messageSource;
    }

    /**
     * Retrieves Checks from database to be confirmed or declined by manager or admin
     *
     * @param model
     * @param currentPage
     * @return
     */
    @GetMapping("/tours-operations/{page}")
    public String getManageableTours(Model model,
                                     @PathVariable(name = "page") int currentPage) {
        log.info("Retrieving paged tours with status \"WAITING_TO_CONFIRM\" to be managed by manager.");
        Page<Check> pagedWaitingChecks = bookingService.getPagedWaitingChecks(currentPage - 1);
        List<Integer> pagesSequence = PagingSequenceCreator.getPagingSequence(
                1, pagedWaitingChecks.getTotalPages());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("waitingChecks", pagedWaitingChecks.getContent());
        model.addAttribute("pagesSequence", pagesSequence);
        model.addAttribute("dollarCourse", dollarCourse);
        return "manager/managerTourPage";
    }

    @PostMapping("/check/decline/{checkId}")
    public RedirectView declineBooking(@PathVariable(name = "checkId") Long checkId,
                                       final RedirectAttributes redirectAttributes) {
        log.info("Declining booking by checkId. Check ID: {}", checkId);
        bookingService.declineBooking(checkId);
        redirectAttributes.addFlashAttribute(
                ATTRIBUTE_TO_PASS_IF_CONFIRMED, "response.message.book_declined"
        );
        return new RedirectView("/manager/confirmed-operation", true);
    }

    @PostMapping("/check/confirm/{checkId}")
    public RedirectView confirmBooking(@PathVariable(name = "checkId") Long checkId,
                                       final RedirectAttributes redirectAttributes) {
        log.info("Confirming booking by checkId. Check ID: {}", checkId);
        bookingService.confirmBooking(checkId);
        redirectAttributes.addFlashAttribute(
                ATTRIBUTE_TO_PASS_IF_CONFIRMED, "response.message.book_confirmed"
        );
        return new RedirectView("/manager/confirmed-operation", true);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/confirmed-operation")
    public String confirmOperationPage(Model model,
                                       @ModelAttribute(ATTRIBUTE_TO_PASS_IF_CONFIRMED) final String message) {
        log.info("Redirected to PRG-page.");
        model.addAttribute(ATTRIBUTE_TO_PASS_IF_CONFIRMED, message);
        model.addAttribute("href", "/manager/tours-operations/1");
        model.addAttribute("hrefDescription", "response.message.go_to_manager_page");
        return "singleMessagePage";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotPresentInDatabaseException.class})
    public String handleException(Model model,
                                  PropertyLocalizedException ex,
                                  Locale locale) {
        log.warn("Handling exception in ManagerController. Exception: {}",ex.getMessage());
        model.addAttribute("error", messageSource.getMessage(
                ex.getPropertyExceptionCode(), null, locale));
        return "error";
    }
}
