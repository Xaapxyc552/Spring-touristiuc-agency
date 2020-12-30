package ua.skidchenko.touristic_agency.service.util;

import org.springframework.data.domain.Sort;
import ua.skidchenko.touristic_agency.entity.enums.TourType;

import java.util.List;

public class TourSortingHolder {
    private Sort sorting;
    private List<TourType> tourTypes;

    public TourSortingHolder() {
    }

    public TourSortingHolder(Sort sorting, List<TourType> tourTypes) {
        this.sorting = sorting;
        this.tourTypes = tourTypes;
    }

    public Sort getSorting() {
        return sorting;
    }

    public void setSorting(Sort sorting) {
        this.sorting = sorting;
    }

    public List<TourType> getTourTypes() {
        return tourTypes;
    }

    public void setTourTypes(List<TourType> tourTypes) {
        this.tourTypes = tourTypes;
    }
}
