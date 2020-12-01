package ua.skidchenko.touristic_agency.service;

import org.springframework.data.domain.Page;
import ua.skidchenko.touristic_agency.controller.enums.OrderOfTours;
import ua.skidchenko.touristic_agency.dto.TourDTO;
import ua.skidchenko.touristic_agency.entity.Tour;

public interface TourService {

    Tour saveTourToDB(Tour tour);

    Page<Tour> getPagedWaitingToursOrderedByArgs(OrderOfTours orderOfTours, String direction, int currentPage);

    Tour saveNewTour(TourDTO tourDTO);

    TourDTO getWaitingTourDTOById(Long tourId);

    Tour updateTourAfterChanges(TourDTO tourDTO);

    Tour markTourAsDeleted(Long tourId);
}
