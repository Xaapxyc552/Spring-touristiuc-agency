package ua.skidchenko.registrationform.dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDTO {

    private String firstname;
    private String email;
    private String password;

}
