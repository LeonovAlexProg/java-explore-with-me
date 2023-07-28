package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.location.LocationDto;
import com.leonovalexprog.model.EventLocation;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LocationMapper {
    public static LocationDto toDto(EventLocation eventLocation) {
        return LocationDto.builder()
                .lon(eventLocation.getLon())
                .lat(eventLocation.getLat())
                .build();
    }
}
