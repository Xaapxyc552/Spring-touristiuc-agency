package ua.skidchenko.touristic_agency.entity;

import lombok.*;
import ua.skidchenko.touristic_agency.entity.enums.HotelType;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.entity.enums.TourType;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"description","tourTypes"})
@EqualsAndHashCode

@Entity
@Table(name = "tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  //TODO
//    @Column(name = "name_ua",nullable = false, length = 50, unique = true)
//    private String nameUA;

    @Column(name = "name",nullable = false, length = 50, unique = true)
    private String name;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false)
    private boolean burning;

    @Column(name = "amount_of_persons", nullable = false)
    private int amountOfPersons;

    @Column(nullable = false)
    private Long price;

    @Enumerated(EnumType.STRING)
    @Column(name = "tour_status")
    private TourStatus tourStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tour__tour_type",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_type_id"))
    private List<TourType> tourTypes;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "hotel_type")
    private HotelType hotelType;
}
