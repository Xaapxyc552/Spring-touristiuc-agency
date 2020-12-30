package ua.skidchenko.touristic_agency.exceptions;

public class UsernameNotFoundException extends PropertyLocalizedException {

    private static final String MESSAGE_CODE = "username.not.found";

    @Override
    public String getPropertyExceptionCode() {
        return MESSAGE_CODE;
    }

    public UsernameNotFoundException() {
        super();
    }

    public UsernameNotFoundException(String message) {
        super(message);
    }

    public UsernameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
