package ua.skidchenko.registrationform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "ua.skidchenko")
@EnableJpaRepositories("ua.skidchenko.registrationform.repository")
public class RegistrationFormApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationFormApplication.class, args);
    }

}
