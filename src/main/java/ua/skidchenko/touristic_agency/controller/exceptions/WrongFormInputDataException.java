package ua.skidchenko.touristic_agency.controller.exceptions;

import java.util.List;

public class WrongFormInputDataException extends RuntimeException {

    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public WrongFormInputDataException(List<String> errors) {
        super();
        this.errors = errors;
    }

    public WrongFormInputDataException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
}
