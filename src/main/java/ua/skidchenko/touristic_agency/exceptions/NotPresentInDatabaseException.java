package ua.skidchenko.touristic_agency.exceptions;


public class NotPresentInDatabaseException extends PropertyLocalizedException {
    private static final String MESSAGE_CODE = "not.present.in.database";

    @Override
    public String getPropertyExceptionCode() {
        return MESSAGE_CODE;
    }

    public NotPresentInDatabaseException() {
        super();
    }

    public NotPresentInDatabaseException(String message) {
        super(message);
    }

    public NotPresentInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }


}
