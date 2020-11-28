package ua.skidchenko.registrationform.entity;

import lombok.*;
import ua.skidchenko.registrationform.entity.enums.HotelType;
import ua.skidchenko.registrationform.entity.enums.TourStatus;
import ua.skidchenko.registrationform.entity.enums.TourType;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @Column(name = "tour_types")
    private List<TourType> tourTypes;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "hotel_type")
    private HotelType hotelType;
}
