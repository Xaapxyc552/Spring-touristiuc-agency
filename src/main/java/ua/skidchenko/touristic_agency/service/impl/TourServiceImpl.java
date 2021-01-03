package ua.skidchenko.touristic_agency.service.impl;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.skidchenko.touristic_agency.controller.enums.OrderOfTours;
import ua.skidchenko.touristic_agency.dto.TourDTO;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.entity.enums.TourType;
import ua.skidchenko.touristic_agency.exceptions.NotPresentInDatabaseException;
import ua.skidchenko.touristic_agency.exceptions.TourNotPresentInDBException;
import ua.skidchenko.touristic_agency.repository.TourRepository;
import ua.skidchenko.touristic_agency.service.TourService;
import ua.skidchenko.touristic_agency.service.util.TourSortingHolder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TourServiceImpl implements TourService {

    private static final String PRIMARY_SORTING_PROPERTY = "burning";

    @Value("${page.size}")
    private int pageSize;

    final
    TourRepository tourRepository;

    final
    Map<String, TourSortingHolder> cacheOfUsersSorts;

    public TourServiceImpl(TourRepository tourRepository,
                           @Qualifier("cacheOfUsersSorts") Map<String, TourSortingHolder> cacheOfUsersSorts) {
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
                                                        List<String> tourTypes,
                                                        String direction,
                                                        int currentPage) {
        log.info("Retrieving ordered paged tours with status \"WAITING\" from DB.");
        Sort.Order orderByPrimary;
        Sort.Order orderByUserSettings;
        TourSortingHolder userSortingHolder;

        String sessionId = ((WebAuthenticationDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getDetails())
                .getSessionId();

        if (orderOfTours != null && direction != null) {
            orderByPrimary = new Sort.Order(Sort.Direction.DESC, PRIMARY_SORTING_PROPERTY);
            orderByUserSettings = new Sort.Order(
                    Sort.Direction.fromString(direction), orderOfTours.getPropertyToSort()
            );
            userSortingHolder = new TourSortingHolder();
            userSortingHolder.setSorting(Sort.by(Arrays.asList(orderByPrimary, orderByUserSettings)));
            if (tourTypes != null && !tourTypes.isEmpty()) {
                userSortingHolder.setTourTypes(TourType.getTourTypesFromStringList(tourTypes));
            } else {
                userSortingHolder.setTourTypes(TourType.getEnumMembersAsList());
            }
            cacheOfUsersSorts.put(sessionId, userSortingHolder);
        } else {
            userSortingHolder = getSortFromCacheElseGetDefault(sessionId);
        }
        PageRequest pr = PageRequest.of(currentPage, pageSize, userSortingHolder.getSorting());
        return tourRepository.findDistinctByTourTypesInAndTourStatus(pr, userSortingHolder.getTourTypes(), TourStatus.WAITING);
    }

    @Override
    public Tour saveNewTour(TourDTO tourDTO) {
        log.info("Saving new into DB tour built from DTO: " + tourDTO.toString());
        Tour newTour = Tour.buildNewTourFromTourDTO(tourDTO);
        return tourRepository.save(newTour);
    }

    @Override
    public TourDTO getWaitingTourDTOById(Long tourId) {
        log.info("Retrieving new tourDTO from DB by tour ID. Tour ID: " + tourId);
        Tour tour = tourRepository.findByIdAndTourStatusIn
                (tourId, Collections.singletonList(TourStatus.WAITING))
                .orElseThrow(() -> {
                    log.warn("Tour not present in DB. Tour ID:" + tourId);
                    throw new TourNotPresentInDBException("Tour not present in DB. Tour ID:" + tourId);
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
                                .map(TourType::getType)
                                .map(Enum::name)
                                .collect(Collectors.toList())
                ).build();
    }

    @Override
    @Transactional
    public Tour updateTourAfterChanges(TourDTO tourDTO) {
        log.info("Updating tour with data from tourDTO: " + tourDTO.toString());
        Optional<Tour> byId = tourRepository.findByIdAndTourStatusIn(Long.valueOf(tourDTO.getId()),
                Collections.singletonList(TourStatus.WAITING));
        if (!byId.isPresent()) {
            throw new TourNotPresentInDBException("Tour was deleted from DB during editing.");
        }
        Tour tourToSave = Tour.buildNewTourFromTourDTO(tourDTO);
        tourRepository.save(tourToSave);
        return tourToSave;
    }

    @Override
    @Transactional
    public Tour markTourAsDeleted(Long tourId) {
        log.info("Marking tour as deleted. TourID: " + tourId);
        Tour tour = tourRepository.findByIdAndTourStatus(tourId, TourStatus.WAITING).orElseThrow(
                () -> {
                    log.warn("Waiting tour is not present id DB. Tour id: " + tourId);
                    throw new TourNotPresentInDBException("Waiting tour is not present id DB. Tour id:" + tourId);
                }
        );
        tour.setTourStatus(TourStatus.DELETED);
        Tour save = tourRepository.save(tour);
        log.info("Tour marked as deleted. Tour id: " + tourId);
        return save;
    }

    private TourSortingHolder getSortFromCacheElseGetDefault(String sessionId) {
        log.info("Getting user's sorting from cache by username. Username: " + sessionId);
        TourSortingHolder tourSortingHolder;
        if (sessionId != null && (tourSortingHolder = cacheOfUsersSorts.get(sessionId)) != null) {
            return tourSortingHolder;
        }
        Sort.Order orderByBurning = new Sort.Order(Sort.Direction.DESC, PRIMARY_SORTING_PROPERTY);
        Sort.Order orderByHotelType = new Sort.Order(
                Sort.Direction.DESC, OrderOfTours.HOTEL_TYPE.getPropertyToSort()
        );
        return new TourSortingHolder(
                Sort.by(Arrays.asList(orderByBurning, orderByHotelType)),
                TourType.getEnumMembersAsList());

    }


}
