package ua.skidchenko.registrationform.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class NotPresentInDatabaseException extends RuntimeException {
    public NotPresentInDatabaseException() {
        super();
    }

    public NotPresentInDatabaseException(String message) {
        super(message);
    }

    public NotPresentInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return ResourceBundle.getBundle("exceptions", Locale.forLanguageTag("ua-UK"))
                .getString(getMessage());
    }
}
