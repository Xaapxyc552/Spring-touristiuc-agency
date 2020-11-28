package ua.skidchenko.registrationform.controller;

public enum OrderOfTours {
    AMOUNT_OF_PERSONS("amountOfPersons")
    ,PRICE("price")
    ,HOTEL_TYPE("hotelType"),
    TOUR_TYPE("tourType");

    OrderOfTours(String propertyToSort) {
        this.propertyToSort = propertyToSort;
    }

    private final String propertyToSort;

    public String getPropertyToSort() {
        return propertyToSort;
    }

}
