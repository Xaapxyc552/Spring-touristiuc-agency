package ua.skidchenko.registrationform.service;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import ua.skidchenko.registrationform.controller.OrderOfTours;
import ua.skidchenko.registrationform.entity.Tour;
import ua.skidchenko.registrationform.entity.enums.TourStatus;
import ua.skidchenko.registrationform.repository.TourRepository;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

@Service
@Log4j2
public class TourServiceImpl implements TourService {

    @Value("${page.size}")
    private Integer pageSize;

    final
    TourRepository tourRepository;

    final
    Map<String, Sort> cacheOfUsersSorts;

    public TourServiceImpl(TourRepository tourRepository,
                           @Qualifier("cacheOfUsersSorts") Map<String, Sort> cacheOfUsersSorts) {
        this.tourRepository = tourRepository;
        this.cacheOfUsersSorts = cacheOfUsersSorts;
    }

    @Override
    public Tour saveTourToDB(Tour tour) {
        return tourRepository.save(tour);
    }

    @Override
    public Page<Tour> getPagedWaitingToursOrderedByArgs(OrderOfTours orderOfTours,
                                                        String direction,
                                                        int page) {
        Sort.Order orderByBurning;
        Sort.Order orderByUserSettings;
        Sort sorting;
        String sessionId = ((WebAuthenticationDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getDetails())
                .getSessionId();

        if (orderOfTours != null && direction != null) {
            orderByBurning = new Sort.Order(Sort.Direction.DESC, "burning");
            orderByUserSettings = new Sort.Order(
                    Sort.Direction.fromString(direction), orderOfTours.getPropertyToSort()
            );
            sorting = Sort.by(Arrays.asList(orderByBurning, orderByUserSettings));
            cacheOfUsersSorts.put(sessionId, sorting);
        } else {
            sorting = getSortFromCacheElseGetDefault(sessionId);
        }
        Example<Tour> example = getTourExampleByStatus(TourStatus.WAITING);
        PageRequest pr = PageRequest.of(page, pageSize, sorting);
        return tourRepository.findAll(example, pr);
    }

    @Override
    public Page<Tour> getPagedWaitingTours(int page) {
        PageRequest pr = PageRequest.of(page, pageSize);
        return tourRepository.findAllByTourStatus(TourStatus.WAITING, pr);
    }

    private Sort getSortFromCacheElseGetDefault(String username) {
        Sort defaultSort;
        Sort.Order orderByBurning = new Sort.Order(Sort.Direction.DESC, "burning");
        Sort.Order orderByHotelType = new Sort.Order(
                Sort.Direction.DESC, OrderOfTours.HOTEL_TYPE.getPropertyToSort()
        );
        defaultSort = Sort.by(Arrays.asList(orderByBurning, orderByHotelType));
        return cacheOfUsersSorts.computeIfAbsent(username, (n) -> defaultSort);
    }

    @NotNull
    private Example<Tour> getTourExampleByStatus(TourStatus status) {
        ExampleMatcher matcher =
                ExampleMatcher
                        .matchingAll()
                        .withMatcher("tour_status", ExampleMatcher.GenericPropertyMatchers
                                .exact()
                                .ignoreCase())
                        .withIgnoreNullValues()
                        .withIgnorePaths("burning", "price", "amountOfPersons");
        return Example.of(
                Tour
                        .builder()
                        .tourStatus(status)
                        .build()
                , matcher);
    }


}
