package ua.skidchenko.touristic_agency;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.skidchenko.touristic_agency.dto.UserDTO;
import ua.skidchenko.touristic_agency.entity.*;
import ua.skidchenko.touristic_agency.entity.enums.*;
import ua.skidchenko.touristic_agency.repository.UserRepository;
import ua.skidchenko.touristic_agency.service.TourService;
import ua.skidchenko.touristic_agency.service.UserService;

import java.util.Collections;
import java.util.HashMap;

@Log4j2
//@Component
public class OnStartupDatabaseFiller implements CommandLineRunner {

    public static final String UK_UA = "uk_UA";
    public static final String EN_GB = "en_GB";
    final
    UserService userService;

    final
    TourService tourService;

    final
    UserRepository userRepository;

    public OnStartupDatabaseFiller(TourService tourService, UserService userService, UserRepository userRepository) {
        this.tourService = tourService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        HashMap<String, String> name = new HashMap<>();
        name.put(UK_UA,"Українська назва туру 1");
        name.put(EN_GB,"English name of tour 1");
        HashMap<String, String> description = new HashMap<>();
        description.put(UK_UA,"Український опис туру 1");
        description.put(EN_GB,"English description of tour 1");

        Tour newTour = buildNewTour(3,
                true,
                name,
                description,
                HotelType.FOUR_STAR,
                20000L,
                TourStatus.WAITING,
                TourType.getInstanceByType(TourType.Type.RECREATION));

        name = new HashMap<>();
        name.put(UK_UA,"Українська назва туру 2");
        name.put(EN_GB,"English name of tour 2");
        description = new HashMap<>();
        description.put(UK_UA,"Український опис туру 2");
        description.put(EN_GB,"English description of tour 2");

        Tour newTour1 = buildNewTour(8,
                false,
                name,
                description,
                HotelType.FIVE_STAR,
                40000L,
                TourStatus.WAITING,
                TourType.getInstanceByType(TourType.Type.SHOPPING));
        name = new HashMap<>();
        name.put(UK_UA,"Українська назва туру 3");
        name.put(EN_GB,"English name of tour 3");
        description = new HashMap<>();
        description.put(UK_UA,"Український опис туру 3");
        description.put(EN_GB,"English description of tour 3");
        Tour newTour2 = buildNewTour(1,
                false,
                name,
                description,
                HotelType.THREE_STAR,
                25000L,
                TourStatus.WAITING,
                TourType.getInstanceByType(TourType.Type.EXCURSION));
        name = new HashMap<>();
        name.put(UK_UA,"Українська назва туру 4");
        name.put(EN_GB,"English name of tour 4");
        description = new HashMap<>();
        description.put(UK_UA,"Український опис туру 4");
        description.put(EN_GB,"English description of tour 4");
        Tour newTour3 = buildNewTour(2,
                false,
                name,
                description,
                HotelType.ONE_STAR,
                42000L,
                TourStatus.WAITING,
                TourType.getInstanceByType(TourType.Type.RECREATION));
        name = new HashMap<>();
        name.put(UK_UA,"Українська назва туру 5");
        name.put(EN_GB,"English name of tour 5");
        description = new HashMap<>();
        description.put(UK_UA,"Український опис туру 5");
        description.put(EN_GB,"English description of tour 5");
        Tour newTour4 = buildNewTour(5,
                true,
                name,
                description,
                HotelType.THREE_STAR,
                20000L,
                TourStatus.WAITING,
                TourType.getInstanceByType(TourType.Type.SHOPPING));
        name = new HashMap<>();
        name.put(UK_UA,"Українська назва туру 6");
        name.put(EN_GB,"English name of tour 6");
        description = new HashMap<>();
        description.put(UK_UA,"Український опис туру 6");
        description.put(EN_GB,"English description of tour 6");
        Tour newTour5 = buildNewTour(4,
                false,
                name,
                description,
                HotelType.TWO_STAR,
                38200L,
                TourStatus.WAITING,
                TourType.getInstanceByType(TourType.Type.EXCURSION));

        name = new HashMap<>();
        name.put(UK_UA,"Українська назва туру 7");
        name.put(EN_GB,"English name of tour 7");
        description = new HashMap<>();
        description.put(UK_UA,"Український опис туру 7");
        description.put(EN_GB,"English description of tour 7");
        Tour newTour6 = buildNewTour(5,
                false,
                name,
                description,
                HotelType.FOUR_STAR,
                43250L,
                TourStatus.WAITING,
                TourType.getInstanceByType(TourType.Type.RECREATION));

        name = new HashMap<>();
        name.put(UK_UA,"Українська назва туру 8");
        name.put(EN_GB,"English name of tour 8");
        description = new HashMap<>();
        description.put(UK_UA,"Український опис туру 8");
        description.put(EN_GB,"English description of tour 8");
        Tour newTour7 = buildNewTour(3,
                false,
                name,
                description,
                HotelType.FIVE_STAR,
                82200L,
                TourStatus.WAITING,
                TourType.getInstanceByType(TourType.Type.RECREATION));


        log.info(tourService.saveTourToDB(newTour));
        log.info(tourService.saveTourToDB(newTour1));
        log.info(tourService.saveTourToDB(newTour2));
        log.info(tourService.saveTourToDB(newTour3));
        log.info(tourService.saveTourToDB(newTour4));
        log.info(tourService.saveTourToDB(newTour5));
        log.info(tourService.saveTourToDB(newTour6));
        log.info(tourService.saveTourToDB(newTour7));
    }

    private Tour buildNewTour(int amountOfPersons,
                              boolean burning,
                              HashMap<String, String> name,
                              HashMap<String, String> description,
                              HotelType hotelType,
                              long price,
                              TourStatus tourStatus,
                              TourType tourType) {
        return Tour.builder().tourStatus(tourStatus)
                .amountOfPersons(amountOfPersons)
                .description(description)
                .hotelType(hotelType)
                .price(price)
                .tourTypes(Collections.singletonList(tourType))
                .burning(burning)
                .name(name)
                .build();
    }
}
