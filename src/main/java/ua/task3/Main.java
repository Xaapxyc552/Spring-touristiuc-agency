package ua.task3;

import ua.task3.controller.Controller;
import ua.task3.model.Model;
import ua.task3.view.View;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new View(), new Model());

        controller.processUser();
    }
}
