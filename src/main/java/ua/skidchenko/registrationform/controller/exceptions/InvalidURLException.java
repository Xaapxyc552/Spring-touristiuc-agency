package ua.skidchenko.registrationform.controller.exceptions;

public class InvalidURLException extends RuntimeException{
    public InvalidURLException(String message) {
        super(message);
    }
}
