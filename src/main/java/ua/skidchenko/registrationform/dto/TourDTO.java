package ua.skidchenko.registrationform.dto;

import lombok.*;
import ua.skidchenko.registrationform.entity.enums.HotelType;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

public class TourDTO {

    @NotNull(message = "Name field null!")
    @NotBlank(message = "Name field blank!")
    @Size(min = 1, max = 100, message = "Firstname field not in size 2 - 100.")
    private String name;

    @NotNull(message = "Description field null!")
    @NotBlank(message = "Description field blank!")
    @Size(min = 20, max = 1000, message = "Username field not in size 20 - 1000.")
    private String description;

    @NotNull(message = "Amount of persons field null!")
    @NotBlank(message = "Amount of persons field blank!")
    private int amountOfPersons;

    @NotNull(message = "Price field null!")
    @NotBlank(message = "Price field blank!")
    @Pattern(regexp = "^[0-9]*$", message = "Price should be integer positive!")
    private Long price;

    @NotNull(message = "Tour types field null!")
    @NotBlank(message = "Tour types field blank!")
    private String tourTypes;

    @NotNull(message = "Hotel type of persons field null!")
    @NotBlank(message = "Hotel type field blank!")
    private HotelType hotelType;

}
