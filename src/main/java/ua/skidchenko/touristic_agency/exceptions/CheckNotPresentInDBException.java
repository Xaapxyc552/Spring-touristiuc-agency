package ua.skidchenko.touristic_agency.exceptions;

public class CheckNotPresentInDBException extends PropertyLocalizedException {

    private static final String MESSAGE_CODE = "check.not.present.in.database";

    @Override
    public String getPropertyExceptionCode() {
        return MESSAGE_CODE;
    }

    public CheckNotPresentInDBException(String message) {
        super(message);
    }

}
