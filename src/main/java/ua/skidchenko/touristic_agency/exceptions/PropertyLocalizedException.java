package ua.skidchenko.touristic_agency.exceptions;

public abstract class PropertyLocalizedException extends RuntimeException {


    public abstract String getPropertyExceptionCode();

    public PropertyLocalizedException() {
        super();
    }

    public PropertyLocalizedException(String message) {
        super(message);
    }

    public PropertyLocalizedException(String message, Throwable cause) {
        super(message, cause);
    }

}
