package ua.skidchenko.registrationform.service;

import org.springframework.data.domain.Page;
import ua.skidchenko.registrationform.controller.enums.OrderOfTours;
import ua.skidchenko.registrationform.dto.TourDTO;
import ua.skidchenko.registrationform.entity.Tour;

public interface TourService {

    Tour saveTourToDB(Tour tour);

    Page<Tour> getPagedWaitingToursOrderedByArgs(OrderOfTours orderOfTours, String direction, int page);

    Page<Tour> getPagedWaitingTours(int page);

    Page<Tour> getRegisteredTours(int page);

    Tour saveNewTour(TourDTO tourDTO);

    TourDTO getWaitingTourDTOById(Long tourId);

    Tour updateTourAfterChanges(TourDTO tourDTO);
}
