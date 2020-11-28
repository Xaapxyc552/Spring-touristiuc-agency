package ua.skidchenko.registrationform.service;

import org.springframework.data.domain.Page;
import ua.skidchenko.registrationform.controller.OrderOfTours;
import ua.skidchenko.registrationform.entity.Tour;

public interface TourService {

    Tour saveTourToDB(Tour tour);

    Page<Tour> getPagedWaitingToursOrderedByArgs(OrderOfTours orderOfTours, String direction, int page);

    Page<Tour> getPagedWaitingTours(int page);
}
