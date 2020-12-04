package ua.skidchenko.touristic_agency.dto;

import lombok.*;
import ua.skidchenko.touristic_agency.entity.enums.HotelType;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString

public class TourDTO {

    private String id;

    @NotNull(message = "Name field null!")
    @NotBlank(message = "Name field blank!")
    private Map<String,String> name;

    @NotNull(message = "Description field null!")
    @NotBlank(message = "Description field blank!")
    private Map<String,String> description;

    @NotNull(message = "Amount of persons field null!")
    @NotBlank(message = "Amount of persons field blank!")
    @Pattern(regexp = "^[0-9]*$", message = "Amount of persons should be integer positive!")
    private String amountOfPersons;

    @NotNull(message = "Price field null!")
    @NotBlank(message = "Price field blank!")
    @Pattern(regexp = "^[0-9]*$", message = "Price should be integer positive!")
    private String price;

    @NotNull(message = "Tour types field null!")
    private List<String> tourTypes;

    @NotNull(message = "Hotel type of persons field null!")
    private HotelType hotelType;

    private String burning;

}
