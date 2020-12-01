package ua.skidchenko.registrationform.service;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import ua.skidchenko.registrationform.controller.enums.OrderOfTours;
import ua.skidchenko.registrationform.dto.TourDTO;
import ua.skidchenko.registrationform.entity.Tour;
import ua.skidchenko.registrationform.entity.enums.TourStatus;
import ua.skidchenko.registrationform.entity.enums.TourType;
import ua.skidchenko.registrationform.exceptions.NotPresentInDatabaseException;
import ua.skidchenko.registrationform.repository.TourRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TourServiceImpl implements TourService {

    private static final String PRIMARY_SORTING_PROPERTY = "burning";

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
        log.info("Saving tour to DB: " + tour.toString());
        return tourRepository.save(tour);
    }

    @Override
    public Page<Tour> getPagedWaitingToursOrderedByArgs(OrderOfTours orderOfTours,
                                                        String direction,
                                                        int currentPage) {
        log.info("Retrieving paged tours with status \"WAITING\" from DB ordered by order:" + orderOfTours.name() +
                ". Direction: " + direction + ". Current page: " + currentPage + ".");
        Sort.Order orderByPrimary;
        Sort.Order orderByUserSettings;
        Sort sorting;
        String sessionId = ((WebAuthenticationDetails) SecurityContextHolder     //TODO Можно ли так делать?
                .getContext()
                .getAuthentication()
                .getDetails())
                .getSessionId();

        if (orderOfTours != null && direction != null) {
            orderByPrimary = new Sort.Order(Sort.Direction.DESC, PRIMARY_SORTING_PROPERTY);
            orderByUserSettings = new Sort.Order(
                    Sort.Direction.fromString(direction), orderOfTours.getPropertyToSort()
            );
            sorting = Sort.by(Arrays.asList(orderByPrimary, orderByUserSettings));
            cacheOfUsersSorts.put(sessionId, sorting);
        } else {
            sorting = getSortFromCacheElseGetDefault(sessionId);
        }
        Example<Tour> example = getTourExampleByStatus(TourStatus.WAITING);
        PageRequest pr = PageRequest.of(currentPage, pageSize, sorting);
        return tourRepository.findAll(example, pr);
    }

    @Override
    public Page<Tour> getPagedWaitingTours(int page) {
        log.info("Retrieving paged tours from DB with status \"WAITING\". Current page:" + page);
        PageRequest pr = PageRequest.of(page, pageSize);
        return tourRepository.findAllByTourStatus(TourStatus.WAITING, pr);
    }

    @Override
    public Page<Tour> getRegisteredTours(int page) {
        log.info("Retrieving tours from DB with status \"REGISTERED\".");
        Example<Tour> example = getTourExampleByStatus(TourStatus.REGISTERED);
        PageRequest pr = PageRequest.of(page, pageSize);
        return tourRepository.findAll(example, pr);
    }

    @Override
    public Tour saveNewTour(TourDTO tourDTO) {
        log.info("Saving new into DB tour built from DTO: " + tourDTO.toString());
        Tour newTour = buildNewTourFromTourDTO(tourDTO);
        return tourRepository.save(newTour);
    }

    @Override
    public TourDTO getWaitingTourDTOById(Long tourId) {
        log.info("Retrieving new tourDTO from DB by tour ID. Tour ID: " + tourId);
        Tour tour = tourRepository.findByIdAndTourStatusIn
                (tourId, Collections.singletonList(TourStatus.WAITING))
                .orElseThrow(() -> {
                    log.warn("Tour not present in DB. Tour ID:" + tourId);
                    throw new NotPresentInDatabaseException("Tour not present in DB. Tour ID:" + tourId);
                });
        return TourDTO.builder()
                .id(String.valueOf(tour.getId()))
                .amountOfPersons(String.valueOf(tour.getAmountOfPersons()))
                .description(tour.getDescription())
                .name(tour.getName())
                .price(String.valueOf(tour.getPrice()))
                .hotelType(tour.getHotelType())
                .tourTypes(
                        tour.getTourTypes().stream()
                                .map(String::valueOf)
                                .collect(Collectors.toList())
                ).build();
    }

    @Override
    public Tour updateTourAfterChanges(TourDTO tourDTO) {
        log.info("Updating tour with data from tourDTO: " + tourDTO.toString());
        Tour tourToSave = buildNewTourFromTourDTO(tourDTO);
        tourRepository.save(tourToSave);
        return tourToSave;
    }

    private Sort getSortFromCacheElseGetDefault(String username) {
        log.info("Getting user's sorting from cache by username. Username: " + username);
        return cacheOfUsersSorts.computeIfAbsent(username, (n) -> {
            Sort.Order orderByBurning = new Sort.Order(Sort.Direction.DESC, PRIMARY_SORTING_PROPERTY);
            Sort.Order orderByHotelType = new Sort.Order(
                    Sort.Direction.DESC, OrderOfTours.HOTEL_TYPE.getPropertyToSort()
            );
            return Sort.by(Arrays.asList(orderByBurning, orderByHotelType));
        });
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

    private Tour buildNewTourFromTourDTO(TourDTO tourDTO) {
        log.info("Building new tour from DTO to save or to update edited tour. TourDTO: " + tourDTO.toString());
        Tour build = Tour.builder()
                .tourStatus(TourStatus.WAITING)
                .hotelType(tourDTO.getHotelType())
                .description(tourDTO.getDescription())
                .price(Long.valueOf(tourDTO.getPrice()))
                .name(tourDTO.getName())
                .amountOfPersons(
                        Integer.parseInt(tourDTO.getAmountOfPersons())
                )
                .tourTypes(
                        TourType.getTourTypesFromStringList(tourDTO.getTourTypes()
                        )
                ).build();
        if (tourDTO.getBurning() != null) {
            build.setBurning(Boolean.parseBoolean(tourDTO.getBurning()));
        }
        if (tourDTO.getId() != null) {
            build.setId(Long.valueOf(tourDTO.getId()));
        }
        return build;
    }


}
