package ua.skidchenko.touristic_agency.exceptions;

public class UsernameExistsException extends PropertyLocalizedException{
    private static final String MESSAGE_CODE = "username.exists";

    @Override
    public String getPropertyExceptionCode() {
        return MESSAGE_CODE;
    }

    public UsernameExistsException() {
        super();
    }

    public UsernameExistsException(String message) {
        super(message);
    }

    public UsernameExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
