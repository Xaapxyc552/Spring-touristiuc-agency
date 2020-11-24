package ua.skidchenko.registrationform.dto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class UserDTO {

    @NotNull(message = "Firstname field null!")
    @NotBlank(message = "Firstname field blank!")
    @Size(min = 2, max = 20, message = "Firstname field not in size 2 - 20.")
    @Pattern(regexp = "[A-Za-zА-Яа-я]{1,20}",
            message = "Firstname field should match regexp [A-Za-zА-Яа-я]{1,20}")
    private String firstname;

    @NotNull(message = "Login field null!")
    @NotBlank(message = "Login field blank!")
    @Size(min = 8, max = 30, message = "Login field not in size 2 - 30.")
    @Pattern(regexp = "[A-Za-z]{1,30}",
            message = "Login field should match regexp [A-Za-z]{1,30}")
    private String login;

    @NotNull(message = "Email field null!")
    @NotBlank(message = "Email field blank!")
    @Email
    private String email;

    @NotNull(message = "Password field null!")
    @NotBlank(message = "Password field blank!")
    @Size(min = 8, max = 20,
            message = "Password field not in size 2 - 30.")
    private String password;

}
