package ua.skidchenko.touristic_agency.service;

import org.springframework.data.domain.Page;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.service.client_services.AdminTourService;
import ua.skidchenko.touristic_agency.service.util.TourSortingHolder;


public interface TourService extends AdminTourService {

    Page<Tour> getPagedWaitingToursOrderedByArgs(TourSortingHolder tourSortingHolder);
}
