package ua.task3.controller;

import ua.task3.model.Model;
import ua.task3.view.View;

import java.util.Scanner;

public class Controller {
private View view;
private Model model;

    public Controller(View view, Model model) {
        this.model = model;
        this.view = view;

    }

    public void processUser() {
        Scanner sc = new Scanner(System.in);

//        ua.task3.view.printMessage();
//        checkInputWithRegex(RegexContainer.REGEX_NAME_UA,View.INPUT_DOESNT_MATCH_REGEX_EN);
    }
}
