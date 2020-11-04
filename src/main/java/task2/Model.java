package com.skidchenko.dz2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model {
    public static int RANGE_TOP = PublicConstants.RANGE_TOP;
    public static int RANGE_BOTTOM = PublicConstants.RANGE_BOTTOM;
    private final ArrayList<Integer> checkedNumbers = new ArrayList<>(100);


    private final int guessedNumber;

    public Model() {
        guessedNumber = Model.rand();
    }

    public boolean isRightNumberGreaterThanEntered(int enteredNumber) {
        return enteredNumber > guessedNumber;
    }

    public static int rand() {
        return new Random().nextInt(99) + 1;
    }

    public static int rand(int beg, int end) {
        int range = end - beg;
        Random rnd = new Random();
        return rnd.nextInt(range) + beg + 1;
    }


    public boolean isNumberGuessed(int enteredNumber) {
        return enteredNumber == guessedNumber;
    }

    public void addCheckedNumber(int enteredNumber) {
        checkedNumbers.add(enteredNumber);
    }


    public List<Integer> getCheckedNumbers() {
        return checkedNumbers;
    }

    public int getGuessedNumber() {
        return guessedNumber;
    }


    public boolean isNumberInRange(int enteredNumber) {
        return enteredNumber >= RANGE_BOTTOM && enteredNumber <= RANGE_TOP;
    }

    public boolean isNumberGreaterThanGuessed(int enteredNumber) {
        return enteredNumber > guessedNumber;
    }
}
