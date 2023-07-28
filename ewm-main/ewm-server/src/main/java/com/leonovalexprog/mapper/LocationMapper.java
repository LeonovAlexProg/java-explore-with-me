package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.location.LocationDto;
import com.leonovalexprog.model.Location;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LocationMapper {
    public static LocationDto toDto(Location location) {
        return LocationDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }
}
