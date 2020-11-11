package task2;

import java.util.Scanner;

public class Controller {
    private final Model model;
    private final View view;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    public void execute() {
        Scanner sc = new Scanner(System.in);
        int enteredNumber;
        boolean isNumberGuessed = false;
        boolean isNumberGreater;

        view.printGreetingMessage(Model.RANGE_BOTTOM, Model.RANGE_TOP);
        while (!isNumberGuessed) {
            if (sc.hasNextInt()) {
                enteredNumber = sc.nextInt();
                if (model.isNumberGuessed(enteredNumber)) {
                    ifNumberGuessed(enteredNumber);
                    isNumberGuessed = true;
                } else if (model.isNumberInRange(enteredNumber)) {
                    isNumberGreater = model.isNumberGreaterThanGuessed(enteredNumber);
                    if (isNumberGreater) {
                        ifNumberGreater(enteredNumber);
                    } else {
                        ifNumberSmaller(enteredNumber);
                    }
                } else {
                    view.printMessage(View.NUMBER_OUT_OF_RANGE);
                    view.printCurrentRange(Model.RANGE_BOTTOM, Model.RANGE_TOP);
                }
            } else {
                view.printMessage(View.WRONG_INPUT_TYPE);
            }
        }

    }

    private void ifNumberGuessed(int enteredNumber) {
        model.addCheckedNumber(enteredNumber);
        view.printFinalStatistic(model.getGuessedNumber(), model.getCheckedNumbers());
    }

    private void ifNumberSmaller(int enteredNumber) {
        view.printMessage(View.CONDITION_MESSAGE +
                View.WORD_SMALLER + View.CONDITION_MESSAGE_END);
        Model.RANGE_BOTTOM = enteredNumber;
        view.printCurrentRange(Model.RANGE_BOTTOM, Model.RANGE_TOP);
        model.addCheckedNumber(enteredNumber);
    }

    private void ifNumberGreater(int enteredNumber) {
        view.printMessage(View.CONDITION_MESSAGE +
                View.WORD_GREATER + View.CONDITION_MESSAGE_END);
        Model.RANGE_TOP = enteredNumber;
        view.printCurrentRange(Model.RANGE_BOTTOM, Model.RANGE_TOP);
        model.addCheckedNumber(enteredNumber);
    }
}
