package ua.skidchenko.touristic_agency.controller.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import ua.skidchenko.touristic_agency.entity.enums.TourType;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that holds a number of parameters to compactly pass information
 * about page and sorting of {@link ua.skidchenko.touristic_agency.entity.Tour} to the DAO-layer/
 */

@Getter
@Setter
@AllArgsConstructor
@Builder

@Log4j2
public class TourSortingHolder implements Serializable {
    private static final String PRIMARY_SORTING_PROPERTY = "burning";
    public static final String SORTING_HOLDER = "sortingHolder";

    private Integer currentPage;
    private Sort sorting;
    private transient List<TourType> tourTypes;

    /**
     * Creates new instance of {@link TourSortingHolder} according to passed parameters.
     * If some passed parameters are null, which means that user load next page,
     * using already chosen sorting strategy, retrieves from session instance of
     * {@link TourSortingHolder} attached to it.
     * If retrieved value is null, produces new instance with default values of fields
     * and puts it into the session for further paging requests.
     *
     * @param orderOfTours {@link OrderOfTours} (can be {@code null})
     * @param direction    String representation of sorting direction (can be "ASC", "DESC")
     * @param tourTypes    list of chosen by user {@link TourType} to be presented in the response
     * @param session      Session, attached to request (never {@code null})
     * @return instance to be used in JPA-pagination.
     */
    public static TourSortingHolder getInstanceFromRequestParameters(OrderOfTours orderOfTours,
                                                                     String direction,
                                                                     ArrayList<String> tourTypes,
                                                                     HttpSession session) {
        log.info("Creating new instance of TourSortingHolder for request.");
        TourSortingHolder userSortingHolder;

        if (orderOfTours != null && direction != null) {
            TourSortingHolder.TourSortingHolderBuilder userSortingHolderBuilder = TourSortingHolder.builder()
                    .currentPage(0)
                    .tourTypes(TourType.getTourTypesFromStringList(tourTypes))
                    .sorting(Sort.by(new Sort.Order(Sort.Direction.DESC, PRIMARY_SORTING_PROPERTY),
                            new Sort.Order(Sort.Direction.fromString(direction), orderOfTours.getPropertyToSort())));
            if (!tourTypes.isEmpty()) {
                userSortingHolder = userSortingHolderBuilder
                        .tourTypes(TourType.getTourTypesFromStringList(tourTypes))
                        .build();
            } else {
                userSortingHolder = userSortingHolderBuilder.tourTypes(TourType.getEnumMembersAsList())
                        .build();
            }
            session.setAttribute(SORTING_HOLDER, userSortingHolder);
        } else {
            userSortingHolder = getSortingFromSessionElseGetDefault(session);
        }
        return userSortingHolder;
    }

    private static TourSortingHolder getSortingFromSessionElseGetDefault(HttpSession session) {
        log.info("Retrieving user's sorting from session.");
        Object tourSortingHolder;
        if ((tourSortingHolder = session.getAttribute(SORTING_HOLDER)) != null) {
            return (TourSortingHolder) tourSortingHolder;
        }
        return new TourSortingHolder(
                0,
                Sort.by(Arrays.asList(
                        new Sort.Order(Sort.Direction.DESC, PRIMARY_SORTING_PROPERTY),
                        new Sort.Order(Sort.Direction.DESC, OrderOfTours.HOTEL_TYPE.getPropertyToSort()))),
                TourType.getEnumMembersAsList());

    }

    /**
     * Enum that represents different orders of sorting
     * {@link ua.skidchenko.touristic_agency.entity.Tour}, that retrieved from database.
     * Has a field {@link OrderOfTours#propertyToSort} which contains a
     * name of field, declared in {@link ua.skidchenko.touristic_agency.entity.Tour}
     * to be used as a property for sorting in SQL-query.
     */
    public enum OrderOfTours {
        AMOUNT_OF_PERSONS("amountOfPersons"),
        PRICE("price"),
        HOTEL_TYPE("hotelType");

        OrderOfTours(String propertyToSort) {
            this.propertyToSort = propertyToSort;
        }

        private final String propertyToSort;

        public String getPropertyToSort() {
            return propertyToSort;
        }

    }
}
