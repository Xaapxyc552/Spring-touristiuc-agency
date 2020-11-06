package ua.task3.view;

import java.util.Locale;
import java.util.ResourceBundle;

public class View {
    public static final Object INPUT_DOESNT_MATCH_REGEX_EN = "";
    public ResourceBundle bundle;

    public View() {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag("en"));
    }
}
