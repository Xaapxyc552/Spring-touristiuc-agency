package ua.skidchenko.touristic_agency.exceptions;

public class UsernameExistsExcetion extends RuntimeException{
    public UsernameExistsExcetion() {
        super();
    }

    public UsernameExistsExcetion(String message) {
        super(message);
    }

    public UsernameExistsExcetion(String message, Throwable cause) {
        super(message, cause);
    }
}
