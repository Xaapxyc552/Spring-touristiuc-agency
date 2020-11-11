package ua.task3.controller;

import ua.task3.model.Model;
import ua.task3.model.NotUniqueLoginException;
import ua.task3.view.View;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Class which purpose is to validate entered by user input data
 * using {@link String#matches(String)}, according to regular expressions,
 * contained in the {@link Controller#regExBundle}. If data is valid, using
 * {@link Model#addNewNoteInDb(String, String, String)} adds new {@link ua.task3.model.entities.Note}
 * to others, to store end represent in future.
 */

public class Controller {
    private final View view;
    private final Model model;
    private final ResourceBundle regExBundle;


    /**
     * Creates an instance of {@link Controller} class.
     * {@param language} given to fill {@link Controller#regExBundle} field with
     * {@link ResourceBundle}, that represents property file with
     * written regular expressions in it.
     *
     * @param view
     * @param model
     * @param language - language of messages and UI.
     */
    public Controller(View view, Model model, String language) {
        this.model = model;
        this.view = view;
        this.regExBundle = ResourceBundle.getBundle(
                "regexp", Locale.forLanguageTag(language));
    }

    /**
     * Processes data, received from user using
     * {@link Controller#checkInputWithRegex(Scanner, String, View)},
     * creates new {@link ua.task3.model.entities.Note} adds newly created note to
     * {@link ua.task3.storage.Database} and prints all existing notes
     * using {@link View#printString(String)}.
     */
    public void processUser() {
        Scanner sc = new Scanner(System.in);
        String firstName;
        String nickName;
        String phoneNumber;

        while (true) {
            view.printMessage("enter.name",
                    regExBundle.getString("firstname.regexp"));
            firstName = checkInputWithRegex(sc, regExBundle.getString("firstname.regexp"), view);

            view.printMessage("enter.nickname", regExBundle.getString("nickname.regexp"));
            nickName = checkInputWithRegex(sc, regExBundle.getString("nickname.regexp"), view);

            view.printMessage("enter.phone.number",
                    regExBundle.getString("phone.number.regexp"));
            phoneNumber = checkInputWithRegex(sc, regExBundle.getString("phone.number.regexp"), view);

            try {
                model.addNewNoteInDb(firstName, nickName, phoneNumber);
            } catch (NotUniqueLoginException e) {
                view.printMessage("not.unique.login");
                continue;
            }
            view.printString(model.notebookToString());
            break;
        }
    }

    /**
     * Returns a {@link String} that contains data, entered by user.
     * Uses {@param sc} to receive data from user in loop,
     * which active until entered string will satisfy {@param regEx} regular expression.
     *
     * @param sc    - Scanner, to receive data from user.
     * @param regEx - regular expression, which data from scanner must satisfy.
     * @param view  - View, to print messages into UI.
     * @return String, entered by user, that satisfy regular expression in {@param regEx}
     */
    private String checkInputWithRegex(Scanner sc, String regEx, View view) {
        String input = null;

        while (sc.hasNextLine() && !((input = sc.nextLine()).matches(regEx))) {
            view.printMessage("wrong.input", regEx);
        }
        return input;
    }


}
