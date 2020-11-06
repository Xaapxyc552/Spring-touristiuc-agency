package ua.task3.controller;

import ua.task3.model.Model;
import ua.task3.view.View;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;



public class Controller {
    private final View view;
    private final Model model;
    private final ResourceBundle regExBundle;

    public Controller(View view, Model model) {
        this.model = model;
        this.view = view;
        this.regExBundle = ResourceBundle.getBundle(
                "regexp", Locale.forLanguageTag("ru"));     //"ru" or "en"
    }

    public void processUser() {
        Scanner sc = new Scanner(System.in);
        String firstName;
        String nickName;
        String phoneNumber;

        view.printMessage("enter.name",
                regExBundle.getString("firstname.regexp"));
        firstName = checkInputWithRegex(sc, regExBundle.getString("firstname.regexp"), view);

        view.printMessage("enter.nickname", regExBundle.getString("nickname.regexp"));
        nickName = checkInputWithRegex(sc, regExBundle.getString("nickname.regexp"), view);

        view.printMessage("enter.phone.number",
                regExBundle.getString("phone.number.regexp"));
        phoneNumber = checkInputWithRegex(sc, regExBundle.getString("phone.number.regexp"), view);

        model.addNewNoteInNotebook(firstName,nickName,phoneNumber);
        view.printString(model.notebookToString());
    }


    private String checkInputWithRegex(Scanner sc, String regEx, View view) {
        String input = null;

        while (sc.hasNextLine() && !((input = sc.nextLine()).matches(regEx))) {
            view.printMessage("wrong.input", regEx);
        }
        return input;
    }
}
