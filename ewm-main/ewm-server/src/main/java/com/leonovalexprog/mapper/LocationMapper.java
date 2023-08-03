package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.location.LocationDto;
import com.leonovalexprog.model.Location;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class LocationMapper {
    public static LocationDto toDto(Location location, Map<Long, Long> eventsViews) {
        LocationDto locationDto = LocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .lat(location.getLat())
                .lon(location.getLon())
                .rad(location.getRad())
                .build();

        if (location.getEvents() != null)
            locationDto.setEvents(EventMapper.toShortDto(location.getEvents(), eventsViews));
        else
            locationDto.setEvents(Collections.emptyList());

        return locationDto;
    }

    public static List<LocationDto> toDto(List<Location> locations, Map<Long, Map<Long, Long>> views) {
        return locations.stream()
                .map(l -> LocationMapper.toDto(l, views.get(l.getId())))
                .collect(Collectors.toList());
    }
}
