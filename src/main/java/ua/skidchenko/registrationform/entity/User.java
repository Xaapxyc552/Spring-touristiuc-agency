package ua.skidchenko.registrationform.entity;


import lombok.*;

import javax.persistence.*;

@Data
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

    @Column(name = "email", nullable = false, unique = true, length = 30)
    private String email;

    @Column(name = "firstname", length = 20)
    private String firstname;

    @Column(name = "password",nullable = false, length = 20)
    private String password;

    @Column(name = "login", nullable = false,length = 30)
    private String login;

    protected void setId(Long id) {
        this.id = id;
    }

}
