package ua.skidchenko.registrationform.exceptions;

public class ForbiddenOperationExceprtion extends RuntimeException {
    public ForbiddenOperationExceprtion() {
        super();
    }

    public ForbiddenOperationExceprtion(String message) {
        super(message);
    }

    public ForbiddenOperationExceprtion(String message, Throwable cause) {
        super(message, cause);
    }
}
