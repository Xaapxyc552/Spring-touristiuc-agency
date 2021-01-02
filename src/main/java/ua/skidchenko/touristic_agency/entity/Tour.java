package ua.skidchenko.touristic_agency.entity;

import lombok.*;
import ua.skidchenko.touristic_agency.dto.TourDTO;
import ua.skidchenko.touristic_agency.entity.enums.HotelType;
import ua.skidchenko.touristic_agency.entity.enums.TourStatus;
import ua.skidchenko.touristic_agency.entity.enums.TourType;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"description", "tourTypes"})
@EqualsAndHashCode

@Entity
@Table(name = "tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "name_translation_mapping",
            joinColumns = {@JoinColumn(name = "name_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "lang_code")
    @Column(name = "name")
    private Map<String, String> name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "description_translation_mapping",
            joinColumns = {@JoinColumn(name = "tour_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "lang_code")
    @Column(name = "description")
    private Map<String, String> description;

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

    public static Tour buildNewTourFromTourDTO(TourDTO tourDTO) {
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
