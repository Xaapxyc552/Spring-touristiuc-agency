package ua.task3.view;

import java.util.Locale;
import java.util.ResourceBundle;

public class View {
    public static final String SPACE_SIGN = " ";
    private final ResourceBundle messagesBundle;

    //"ru" or "en"
    public View() {
        messagesBundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag("ru"));
    }

    public void printString(String string) {
        System.out.println(string);
    }

    public void printMessage(String message) {
        System.out.println(this.messagesBundle.getObject(message));
    }
    public void printMessage(String message, String additional) {
        System.out.println(this.messagesBundle.getString(message) + SPACE_SIGN + additional);
    }
}
