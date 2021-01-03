package ua.skidchenko.touristic_agency.entity.enums;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

class TourTypeTest {

    @Test
    void getInstanceByType_correctInput_AssertEqualsForEachType() {
        for (TourType.Type type:TourType.Type.values()) {
            TourType instanceByType = TourType.getInstanceByType(type);
            assertEquals(instanceByType.getType(),type);
        }
    }
}