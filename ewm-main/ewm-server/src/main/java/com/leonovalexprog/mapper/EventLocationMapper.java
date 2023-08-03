package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.location.EventLocationDto;
import com.leonovalexprog.model.EventLocation;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EventLocationMapper {
    public static EventLocationDto toDto(EventLocation eventLocation) {
        return EventLocationDto.builder()
                .lon(eventLocation.getLon())
                .lat(eventLocation.getLat())
                .build();
    }
}
