package ua.skidchenko.touristic_agency.exceptions;

public class IncorrectSumm extends PropertyLocalizedException {

    private static final String MESSAGE_CODE = "incorrect.summ";

    @Override
    public String getPropertyExceptionCode() {
        return MESSAGE_CODE;
    }

    public IncorrectSumm() {
        super();
    }

    public IncorrectSumm(String message) {
        super(message);
    }

    public IncorrectSumm(String message, Throwable cause) {
        super(message, cause);
    }

}
