package task2;

import java.util.List;

public class View {

    public static final String WORD_GREATER = "greater";
    public static final String WORD_SMALLER = "smaller";
    public static final String WRONG_INPUT_TYPE = "Wrong input type! You shall use only integers!\n";
    public static final String GREETING_MESSAGE = "Hello! Try to guess number in range: [%d - %d]\n";
    public static final String NUMBER_OUT_OF_RANGE = "Entered number is out of range!";
    public static final String FINAL_STATISTICS = "You won! Guessed number is %d\n" +
            "Checked numbers: %s\n" +
            "Count of attempts: %d";
    public static final String WRONG_NUMBER_MESSAGE = "Try again. Current range: [%d - %d]\n";
    public static final String CONDITION_MESSAGE = "Entered word is ";
    public static final String CONDITION_MESSAGE_END = " than guessed.";

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printFinalStatistic(int guessedNumber,
                                    List<Integer> checkedNumbers) {
        System.out.printf(FINAL_STATISTICS, guessedNumber,
                checkedNumbers.toString(), checkedNumbers.size());
    }

    public void printCurrentRange(int rangeBottom, int rangeTop) {
        System.out.printf(WRONG_NUMBER_MESSAGE, rangeBottom, rangeTop);
    }

    public void printGreetingMessage(int rangeBottom, int rangeTop) {
        System.out.printf(GREETING_MESSAGE, rangeBottom, rangeTop);

    }
}
