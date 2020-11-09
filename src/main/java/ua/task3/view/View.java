package ua.task3.view;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Class, contains methods to print messages into {@link System#in}.
 * Able to print messages at distinct language, specified in constructor.
 *
 * @see java.util.ResourceBundle
 * @see java.lang.String
 * @see java.util.Locale
 */
public class View {
    public static final String SPACE_SIGN = " ";
    private final ResourceBundle messagesBundle;

    /**
     * Creates new instance of this class, which contains
     * bundle of messages at language, specified by {@param language}.
     *
     * @param language
     */
    public View(String language) {
        messagesBundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag(language));
    }

    /**
     * Prints into {@link System#in} string, received as {@param string}.
     *
     * @param string
     */
    public void printString(String string) {
        System.out.println(string);
    }

    /**
     * Prints into {@link System#in} message, specified in {@link View#messagesBundle},
     * using {@param message} as a key to retrieve corresponding string.
     *
     * @param message
     */
    public void printMessage(String message) {
        System.out.println(this.messagesBundle.getString(message));
    }

    /**
     * Prints into {@link System#in} message, specified in {@link View#messagesBundle},
     * using {@param message} as a key to retrieve corresponding string.
     * Besides, concat to the retrieved string {@param additional},
     * that contains additional information.
     *
     * @param message
     * @param additional
     */
    public void printMessage(String message, String additional) {
        System.out.println(this.messagesBundle.getString(message) + SPACE_SIGN + additional);
    }
}
