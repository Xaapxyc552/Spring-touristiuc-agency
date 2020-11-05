package task2;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ModelTest {
    static Model model;

    @BeforeEach
    public void setUp() throws Exception {
        model = new Model();
    }


    @Test
    void isRightNumberGreaterThanEnteredAssertTrue() {
        int enteredNumber = 30;

        model.setGuessedNumber(10);
        Assertions.assertTrue(model.isNumberGreaterThanGuessed(enteredNumber));
    }

    @Test
    void isRightNumberGreaterThanEnteredAssertFalse() {
        int enteredNumber = 10;

        model.setGuessedNumber(30);
        Assertions.assertFalse(model.isNumberGreaterThanGuessed(enteredNumber));
    }

    @Test
    void testRandWithNoArgs() {
        for (int i = 0; i < 10000; i++) {
            int randNumber = Model.rand();
            Assert.assertTrue(randNumber <= Model.RANGE_TOP &&
                    randNumber >= Model.RANGE_BOTTOM);
        }
    }

    @Test
    void testRandWithArgs() {
        int topOfRange = 50;
        int botOfRange = 5;

        try {
            for (int i = 0; i < 10000; i++) {
                int randNumber = Model.rand(botOfRange, topOfRange);
                Assert.assertTrue(randNumber <= topOfRange &&
                        randNumber >= botOfRange);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    void testRandWithTopLessThanBotArgs() {
        int topOfRange = 50;
        int botOfRange = 5;

        try {
            for (int i = 0; i < 10000; i++) {
                int randNumber = Model.rand(botOfRange, topOfRange);
                Assert.assertTrue(randNumber <= topOfRange &&
                        randNumber >= botOfRange);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void testBotAndTopLimitsEquals() {
        int topOfRange = 5;
        int botOfRange = 5;

        Assertions.assertThrows(Exception.class, () -> Model.rand(botOfRange, topOfRange));
    }

    @Test
    void isNumberGuessedAssertTrue() {
        int enteredNumber = 30;

        model.setGuessedNumber(30);
        Assertions.assertTrue(model.isNumberGuessed(enteredNumber));
    }

    @Test
    void isNumberGuessedAsertFalse() {
        int enteredNumber = 30;

        model.setGuessedNumber(10);
        Assertions.assertFalse(model.isNumberGuessed(enteredNumber));
    }


    @Test
    void addCheckedNumber() {
        List<Integer> checkedNumbers = model.getCheckedNumbers();
        int initialSizeOfList;
        int finalSizeOfList;

        initialSizeOfList = checkedNumbers.size();
        checkedNumbers.add(15);
        finalSizeOfList = checkedNumbers.size();
        Assertions.assertEquals(1, finalSizeOfList - initialSizeOfList);
    }

    @Test
    void isNumberInRangeAssertTrue() {
        int enteredNumber = 15;

        Model.RANGE_BOTTOM = 0;
        Model.RANGE_TOP = 100;
        Assertions.assertTrue(model.isNumberInRange(enteredNumber));
    }

    @Test
    void isNumberInRangeAssertFalse() {
        int enteredNumber = 120;

        Model.RANGE_BOTTOM = 0;
        Model.RANGE_TOP = 100;
        Assertions.assertFalse(model.isNumberInRange(enteredNumber));
    }

    @Test
    void isNumberGreaterThanGuessedAssertTrue() {
        int enteredNumber = 50;

        model.setGuessedNumber(30);
        Assertions.assertTrue(model.isNumberGreaterThanGuessed(enteredNumber));
    }

    @Test
    void isNumberGreaterThanGuessedAssertFalse() {
        int enteredNumber = 30;

        model.setGuessedNumber(50);
        Assertions.assertFalse(model.isNumberGreaterThanGuessed(enteredNumber));
    }
}