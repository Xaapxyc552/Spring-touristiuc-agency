package ua.skidchenko.touristic_agency.exceptions;

public class TourNotPresentInDBException extends PropertyLocalizedException {

    private static final String MESSAGE_CODE = "tour.not.present.in.database";

    @Override
    public String getPropertyExceptionCode() {
        return MESSAGE_CODE;
    }

    public TourNotPresentInDBException(String message) {
        super(message);
    }
}
