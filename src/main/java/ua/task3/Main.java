package ua.task3;

import ua.task3.controller.Controller;
import ua.task3.model.Model;
import ua.task3.view.View;

public class Main {
    public static void main(String[] args) {
        //"ru" or "en"
        String language = "ru";
        Controller controller = new Controller(
                new View(language),
                new Model(),
                language);

        controller.processUser();
    }
}
