package ua.skidchenko.touristic_agency.entity.enums;


public enum HotelType {
    ONE_STAR(1, "*"),
    TWO_STAR(2, "**"),
    THREE_STAR(3, "***"),
    FOUR_STAR(4, "****"),
    FIVE_STAR(5, "*****");

    private final int amountOfStars;
    private final String amountOfStarsPretty;

    HotelType(int i, String s) {
        this.amountOfStars = i;
        this.amountOfStarsPretty = s;
    }

    public int getAmountOfStars() {
        return amountOfStars;
    }

    public String getAmountOfStarsPretty() {
        return amountOfStarsPretty;
    }
}
