package ua.skidchenko.touristic_agency.service;

import org.springframework.data.domain.Page;
import ua.skidchenko.touristic_agency.controller.enums.OrderOfTours;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.entity.enums.TourType;
import ua.skidchenko.touristic_agency.service.client_services.AdminTourService;

import java.util.List;

public interface TourService extends AdminTourService {

    Page<Tour> getPagedWaitingToursOrderedByArgs(OrderOfTours orderOfTours,
                                                 List<String> tourTypes,
                                                 String direction,
                                                 int currentPage);
}
