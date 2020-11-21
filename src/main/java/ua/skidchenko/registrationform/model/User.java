package ua.skidchenko.registrationform.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "user",schema = "registration_form")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "email", nullable = false, unique = true, length = 30)
    private String email;

    @Setter
    @Column(name = "firstname", length = 20)
    private String firstname;

    @Setter
    @Column(name = "password",nullable = false, length = 20)
    private String password;

    protected void setId(Long id) {
        this.id = id;
    }

}
