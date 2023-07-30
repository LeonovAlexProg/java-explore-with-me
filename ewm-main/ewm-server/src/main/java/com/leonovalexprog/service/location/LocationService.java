package com.leonovalexprog.service.location;

import com.leonovalexprog.dto.location.LocationDto;
import com.leonovalexprog.dto.location.NewLocationDto;
import com.leonovalexprog.dto.location.UpdateLocationDto;

import java.util.List;

public interface LocationService {
    LocationDto createNewLocation(NewLocationDto newLocationDto);

    List<LocationDto> findLocations(Float lat, Float lon, Float rad, Boolean closest);

    LocationDto findExactLocation(long locationId);

    LocationDto updateLocation(Long locationId, UpdateLocationDto updateLocationDto);
}
