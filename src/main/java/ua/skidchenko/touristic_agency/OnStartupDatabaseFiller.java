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

import java.util.Arrays;
import java.util.Collections;

@Log4j2
//@Component
public class OnStartupDatabaseFiller implements CommandLineRunner {

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

        UserDTO build = UserDTO.builder()
                .email("asdsad@google.com")
                .firstname("Sanya")
                .password("password").username("user").build();

        UserDTO build1 = UserDTO.builder()
                .email("asdasdsad@google.com")
                .firstname("Sanyaa")
                .password("password").username("admin").build();

        UserDTO build2 = UserDTO.builder()
                .email("asdasdddsad@google.com")
                .firstname("Sanyaaa")
                .password("password").username("manager").build();

        User user = userService.saveUser(build);
        User admin = userService.saveUser(build1);
        User manager = userService.saveUser(build2);

        user.setMoney(200000L);
        admin.setRole(Role.ROLE_ADMIN);
        manager.setRole(Role.ROLE_MANAGER);

        userRepository.save(admin);
        userRepository.save(manager);
        userRepository.save(user);

        Tour newTour = Tour.builder()
                .amountOfPersons(5)
                .burning(false)
                .description("tourdesc")
                .hotelType(HotelType.FIVE_STAR)
                .price(200L)
                .name("name1")
                .tourStatus(TourStatus.WAITING)
                .tourTypes(Collections.singletonList(
                        TourType.getInstanceByType(TourType.Type.RECREATION))
                )
                .build();

        Tour newTour1 = Tour.builder()
                .amountOfPersons(3)
                .burning(true)
                .description("tourdesc")
                .hotelType(HotelType.FOUR_STAR)
                .price(150000L)
                .name("name2")
                .tourStatus(TourStatus.WAITING)
                .tourTypes(Collections.singletonList(
                        TourType.getInstanceByType(TourType.Type.RECREATION))
                )
                .build();

        Tour newTour2 = Tour.builder()
                .amountOfPersons(1)
                .burning(false)
                .description("tourdesc")
                .hotelType(HotelType.TWO_STAR)
                .price(40000L)
                .name("name3")
                .tourStatus(TourStatus.WAITING)
                .tourTypes(Collections.singletonList(
                        TourType.getInstanceByType(TourType.Type.RECREATION))
                )
                .build();

        Tour newTour3 = Tour.builder()
                .amountOfPersons(3)
                .burning(false)
                .description("tourdesc")
                .hotelType(HotelType.TWO_STAR)
                .price(80000L)
                .name("name4")
                .tourStatus(TourStatus.WAITING)
                .tourTypes(Arrays.asList(
                        TourType.getInstanceByType(TourType.Type.RECREATION),
                        TourType.getInstanceByType(TourType.Type.SHOPPING)))
                .build();
        Tour newTour4 = Tour.builder()
                .amountOfPersons(8)
                .burning(true)
                .description("tourdesc")
                .hotelType(HotelType.FOUR_STAR)
                .price(300000L)
                .name("name5")
                .tourStatus(TourStatus.WAITING)
                .tourTypes(Collections.singletonList(
                        TourType.getInstanceByType(TourType.Type.EXCURSION))
                )
                .build();
        Tour newTour5 = Tour.builder()
                .amountOfPersons(2)
                .burning(true)
                .description("tourdesc")
                .hotelType(HotelType.THREE_STAR)
                .price(60000L)
                .name("name6")
                .tourStatus(TourStatus.WAITING)
                .tourTypes(Collections.singletonList(
                        TourType.getInstanceByType(TourType.Type.SHOPPING))
                )
                .build();
        Tour newTour6 = Tour.builder()
                .amountOfPersons(5)
                .burning(true)
                .description("tourdesc")
                .hotelType(HotelType.THREE_STAR)
                .price(120000L)
                .name("name7")
                .tourStatus(TourStatus.WAITING)
                .tourTypes(Collections.singletonList(
                        TourType.getInstanceByType(TourType.Type.RECREATION))
                )
                .build();
        Tour newTour7 = Tour.builder()
                .amountOfPersons(4)
                .burning(true)
                .description("tourdesc")
                .hotelType(HotelType.ONE_STAR)
                .price(50000L)
                .name("name8")
                .tourStatus(TourStatus.WAITING)
                .tourTypes(Arrays.asList(
                        TourType.getInstanceByType(TourType.Type.RECREATION),
                        TourType.getInstanceByType(TourType.Type.EXCURSION),
                        TourType.getInstanceByType(TourType.Type.SHOPPING)))
                .build();

        log.info(tourService.saveTourToDB(newTour));
        log.info(tourService.saveTourToDB(newTour1));
        log.info(tourService.saveTourToDB(newTour2));
        log.info(tourService.saveTourToDB(newTour3));
        log.info(tourService.saveTourToDB(newTour4));
        log.info(tourService.saveTourToDB(newTour5));
        log.info(tourService.saveTourToDB(newTour6));
        log.info(tourService.saveTourToDB(newTour7));
    }
}
