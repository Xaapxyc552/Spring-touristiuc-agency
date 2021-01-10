package ua.skidchenko.touristic_agency.service.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import ua.skidchenko.touristic_agency.entity.enums.TourType;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class TourSortingHolder implements Serializable {
    private Integer currentPage;
    private Sort sorting;
    private transient List<TourType> tourTypes;
}
