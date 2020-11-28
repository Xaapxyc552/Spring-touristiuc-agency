package ua.skidchenko.registrationform.dto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

public class UserDTO {

    @NotBlank(message = "Firstname field blank!")
    @Size(min = 2, max = 20, message = "Firstname field not in size 2 - 20.")
    @Pattern(regexp = "[A-Za-zА-Яа-я]{2,20}",
            message = "Firstname field should match regexp [A-Za-zА-Яа-я]{1,20}")
    private String firstname;

    @NotNull(message = "Username field null!")
    @NotBlank(message = "Username field blank!")
    @Size(min = 2, max = 30, message = "Username field not in size 2 - 30.")
    @Pattern(regexp = "[A-Za-z]{2,30}",
            message = "Username field should match regexp [A-Za-z]{2,30}")
    private String username;

    @NotBlank(message = "Email field blank!")
    @Email
    private String email;

    @NotNull(message = "Password field null!")
    @NotBlank(message = "Password field blank!")
    @Size(min = 8, max = 30,
            message = "Password field not in size 8 - 30.")
    private String password;

}
