package ua.skidchenko.touristic_agency.exceptions;

public class TourNotPresentInDBException extends RuntimeException {
    public TourNotPresentInDBException() {
        super();
    }

    public TourNotPresentInDBException(String message) {
        super(message);
    }

    public TourNotPresentInDBException(String message, Throwable cause) {
        super(message, cause);
    }
}
