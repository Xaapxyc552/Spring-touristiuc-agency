package task1;

public class View {
    public static final String ASK_FOR_HELLO_WORD = "Please write word \"Hello\"";
    public static final String ASK_FOR_WORLD_WORD = "Please write word \"world\"";
    public static final String WRONG_WORD_ATTENTION = "Wrong word! You have to write ";

    public void printAskForWordHello() {
        System.out.println(ASK_FOR_HELLO_WORD);
    }

    public void printAskForWordWorld() {
        System.out.println(ASK_FOR_WORLD_WORD);
    }

    public void printString(String phrase) {
        System.out.println(phrase);
    }

}
