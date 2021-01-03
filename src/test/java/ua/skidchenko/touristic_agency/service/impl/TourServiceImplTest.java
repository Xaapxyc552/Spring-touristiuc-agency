package ua.skidchenko.touristic_agency.service.impl;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ua.skidchenko.touristic_agency.dto.TourDTO;
import ua.skidchenko.touristic_agency.entity.Tour;
import ua.skidchenko.touristic_agency.entity.enums.HotelType;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.entity.enums.TourType;
import ua.skidchenko.touristic_agency.repository.CheckRepository;
import ua.skidchenko.touristic_agency.repository.TourRepository;
import ua.skidchenko.touristic_agency.repository.UserRepository;
import ua.skidchenko.touristic_agency.service.util.TourSortingHolder;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TourServiceImplTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private Map<String, TourSortingHolder> cacheOfUsersSorts;

    @InjectMocks
    TourServiceImpl tourService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(tourService, "pageSize", 5);
    }


    @Test
    void saveTourToDB() {
        Long idAfterPersisting = 1L;
        when(tourRepository.save(any(Tour.class))).thenReturn(Tour.builder().id(idAfterPersisting).build());

        Tour tour = Tour.builder().build();
        Assert.assertNull(tour.getId());

        Tour savedTour = tourService.saveTourToDB(tour);
        Assert.assertEquals(idAfterPersisting, savedTour.getId());
    }


    @Test
    void saveNewTour() {
        Long idAfterPersisting = 1L;
        when(tourRepository.save(any(Tour.class)))
                .thenReturn(Tour.builder().id(idAfterPersisting).build());

        TourDTO tourDTO = TourDTO.builder()
                .amountOfPersons(String.valueOf(3))
                .burning(String.valueOf(true))
                .name(new HashMap<>())
                .description(new HashMap<>())
                .hotelType(HotelType.FOUR_STAR)
                .price(String.valueOf(20000L))
                .tourTypes(TourType.getEnumMembersAsList().stream()
                        .map(n -> n.getType().name()).collect(Collectors.toList())
                )
                .build();

        Assert.assertNull(tourDTO.getId());

        Tour savedTour = tourService.saveNewTour(tourDTO);
        Assert.assertEquals(idAfterPersisting, savedTour.getId());

    }

    @Test
    void updateTourAfterChanges() {
        Long idOfPersistedTour = 1L;
        TourDTO tourDTO = TourDTO.builder()
                .id(String.valueOf(idOfPersistedTour))
                .amountOfPersons(String.valueOf(3))
                .burning(String.valueOf(true))
                .name(new HashMap<>())
                .description(new HashMap<>())
                .hotelType(HotelType.FOUR_STAR)
                .price(String.valueOf(20000L))
                .tourTypes(TourType.getEnumMembersAsList().stream()
                        .map(n -> n.getType().name()).collect(Collectors.toList())
                )
                .build();

        Tour tour = Tour.buildNewTourFromTourDTO(tourDTO);

        when(tourRepository.save(any(Tour.class))).thenReturn(Tour.builder().id(idOfPersistedTour).build());
        when(tourRepository.findByIdAndTourStatusIn(anyLong(),
                anyCollection())).thenReturn(Optional.of(tour));

        Assert.assertNotNull(tourDTO.getId());

        Tour savedTour = tourService.updateTourAfterChanges(tourDTO);
        Assert.assertEquals(tour, savedTour);
    }

    @Test
    void markTourAsDeleted() {
        Long idOfPersistedTour = 1L;
        TourDTO tourDTO = TourDTO.builder()
                .id(String.valueOf(idOfPersistedTour))
                .amountOfPersons(String.valueOf(3))
                .burning(String.valueOf(true))
                .name(new HashMap<>())
                .description(new HashMap<>())
                .hotelType(HotelType.FOUR_STAR)
                .price(String.valueOf(20000L))
                .tourTypes(TourType.getEnumMembersAsList().stream()
                        .map(n -> n.getType().name()).collect(Collectors.toList())
                )
                .build();

        Tour tour = Tour.buildNewTourFromTourDTO(tourDTO);
        Tour deletedTour = Tour.buildNewTourFromTourDTO(tourDTO);
        deletedTour.setTourStatus(TourStatus.DELETED);

        when(tourRepository.findByIdAndTourStatus(anyLong(), eq(TourStatus.WAITING)))
                .thenReturn(Optional.of(tour));

        when(tourRepository.save(any(Tour.class))).thenReturn(deletedTour);

        Tour savedTour = tourService.markTourAsDeleted(idOfPersistedTour);
        Assert.assertEquals(TourStatus.DELETED, savedTour.getTourStatus());
    }


    @Test
    void getPagedWaitingToursOrderedByArgs() {
        //TODO
    }

    @Test
    void getWaitingTourDTOById() {
        //TODO
    }
}