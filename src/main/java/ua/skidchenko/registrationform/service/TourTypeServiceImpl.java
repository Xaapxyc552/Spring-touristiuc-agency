package ua.skidchenko.registrationform.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.skidchenko.registrationform.entity.enums.TourType;
import ua.skidchenko.registrationform.repository.TourTypeRepository;

@Service
@Log4j2
public class TourTypeServiceImpl implements TourTypeService {

    final
    TourTypeRepository tourTypeRepository;

    public TourTypeServiceImpl(TourTypeRepository tourTypeRepository) {
        this.tourTypeRepository = tourTypeRepository;
    }

    @Override
    public TourType saveInstance(TourType tourType) {
        return tourTypeRepository.save(tourType);
    }
}
