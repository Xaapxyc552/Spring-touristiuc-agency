package ua.skidchenko.touristic_agency.exceptions;

public class ForbiddenOperationException extends PropertyLocalizedException {

    private static final String MESSAGE_CODE = "operation.forbidden";

    @Override
    public String getPropertyExceptionCode() {
        return MESSAGE_CODE;
    }

    public ForbiddenOperationException(String message) {
        super(message);
    }
}
