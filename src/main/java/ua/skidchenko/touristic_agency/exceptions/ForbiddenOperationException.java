package ua.skidchenko.touristic_agency.exceptions;

public class ForbiddenOperationException extends PropertyLocalizedException {

    private static final String MESSAGE_CODE = "operation.forbidden";

    @Override
    public String getPropertyExceptionCode() {
        return MESSAGE_CODE;
    }

    public ForbiddenOperationException() {
        super();
    }

    public ForbiddenOperationException(String message) {
        super(message);
    }

    public ForbiddenOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}
