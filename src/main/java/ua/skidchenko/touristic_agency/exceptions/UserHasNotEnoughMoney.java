package ua.skidchenko.touristic_agency.exceptions;

public class UserHasNotEnoughMoney extends PropertyLocalizedException {

    private static final String MESSAGE_CODE = "user.has.not.enough.money";

    @Override
    public String getPropertyExceptionCode() {
        return MESSAGE_CODE;
    }

    public UserHasNotEnoughMoney(String message) {
        super(message);
    }
}
