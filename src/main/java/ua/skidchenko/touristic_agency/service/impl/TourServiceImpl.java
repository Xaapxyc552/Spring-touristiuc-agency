package ua.skidchenko.touristic_agency.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.skidchenko.touristic_agency.dto.TourDTO;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.entity.enums.TourType;
import ua.skidchenko.touristic_agency.exceptions.TourNotPresentInDBException;
import ua.skidchenko.touristic_agency.repository.TourRepository;
import ua.skidchenko.touristic_agency.service.TourService;
import ua.skidchenko.touristic_agency.controller.util.TourSortingHolder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TourServiceImpl implements TourService {


    @Value("${page.size}")
    private int pageSize;

    final
    TourRepository tourRepository;


    public TourServiceImpl(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    @Override
    public Tour saveTourToDB(Tour tour) {
        log.info("Saving tour to DB: {}", tour.toString());
        return tourRepository.save(tour);
    }

    @Override
    public Page<Tour> getPagedWaitingToursOrderedByArgs(TourSortingHolder userSortingHolder) {
        log.info("Retrieving ordered paged tours with status \"WAITING\" from DB.");
        return tourRepository.findDistinctByTourTypesInAndTourStatus(
                PageRequest.of(userSortingHolder.getCurrentPage(), pageSize, userSortingHolder.getSorting()),
                userSortingHolder.getTourTypes(), TourStatus.WAITING);
    }

    @Override
    public Tour saveNewTour(TourDTO tourDTO) {
        log.info("Saving new into DB tour built from DTO: {}", tourDTO.toString());
        return tourRepository.save(Tour.buildNewTourFromTourDTO(tourDTO));
    }

    @Override
    public TourDTO getWaitingTourDTOById(Long tourId) {
        log.info("Retrieving new tourDTO from DB by tour ID. Tour ID: {}", tourId);
        Tour tour = tourRepository.findByIdAndTourStatusIn
                (tourId, Collections.singletonList(TourStatus.WAITING))
                .orElseThrow(() -> {
                    log.warn("Tour not present in DB. Tour ID: {}", tourId);
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
        log.info("Updating tour with data from tourDTO: {}", tourDTO.toString());
        tourRepository.findByIdAndTourStatusIn(Long.valueOf(tourDTO.getId()),
                Collections.singletonList(TourStatus.WAITING)).orElseThrow(() -> {
                    log.warn("Tour was deleted from DB during editing. TourID: {}",tourDTO.getId());
            throw new TourNotPresentInDBException("Tour was deleted from DB during editing.");
        });
        Tour tourToSave = Tour.buildNewTourFromTourDTO(tourDTO);
        tourRepository.save(tourToSave);
        return tourToSave;
    }

    @Override
    @Transactional
    public Tour markTourAsDeleted(Long tourId) {
        log.info("Marking tour as deleted. TourID: {}", tourId);
        Tour tour = tourRepository.findByIdAndTourStatus(tourId, TourStatus.WAITING).orElseThrow(
                () -> {
                    log.warn("Waiting tour is not present id DB. Tour id: {}", tourId);
                    throw new TourNotPresentInDBException("Waiting tour is not present id DB. Tour id:" + tourId);
                }
        );
        tour.setTourStatus(TourStatus.DELETED);
        return tourRepository.save(tour);
    }


}
