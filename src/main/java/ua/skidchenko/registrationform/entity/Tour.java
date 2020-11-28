package ua.skidchenko.registrationform.entity;

import lombok.*;
import ua.skidchenko.registrationform.entity.enums.HotelType;
import ua.skidchenko.registrationform.entity.enums.TourStatus;
import ua.skidchenko.registrationform.entity.enums.TourType;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "description")
@EqualsAndHashCode

@Entity
@Table(name = "tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false)
    private boolean burning;

    @Column(name = "amount_of_persons", nullable = false)
    private int amountOfPersons;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "tour_status")
    private TourStatus tourStatus;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tour_type")
    private TourType tourType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "hotel_type")
    private HotelType hotelType;
}