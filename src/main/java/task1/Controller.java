package com.skidchenko.dz1;

import java.util.Scanner;

public class Controller {
    public static final String WORD_HELLO = "Hello";
    public static final String WORD_WORLD = "world";

    private final Scanner sc;
    private final Model model;
    private final View view;

    public Controller(Scanner sc, Model model, View view) {
        this.sc = sc;
        this.model = model;
        this.view = view;
    }

    public void executeProgram() {
        String firstWord;
        String secondWord;
        String stringToDisplay;

        view.printAskForWordHello();
        firstWord = getNextStringIfEqualsToArg(WORD_HELLO);
        view.printAskForWordWorld();
        secondWord = getNextStringIfEqualsToArg(WORD_WORLD);
        stringToDisplay= model.concatStrings(firstWord, secondWord);
        view.printString(stringToDisplay);
    }

    private String getNextStringIfEqualsToArg(String neededWord) {
        String enteredWord = sc.nextLine();

        while (!enteredWord.equals(neededWord)) {
            view.printString(View.WRONG_WORD_ATTENTION + "\"" + neededWord + "\"");
            enteredWord = sc.nextLine();
        }
        return enteredWord;
    }
}
