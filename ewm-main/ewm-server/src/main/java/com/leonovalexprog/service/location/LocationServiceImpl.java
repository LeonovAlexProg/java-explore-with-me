package com.leonovalexprog.service.location;

import com.leonovalexprog.dto.location.LocationDto;
import com.leonovalexprog.dto.location.NewLocationDto;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.FieldValueExistsException;
import com.leonovalexprog.mapper.LocationMapper;
import com.leonovalexprog.model.Location;
import com.leonovalexprog.repository.EventsRepository;
import com.leonovalexprog.repository.LocationRepository;
import com.leonovalexprog.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final EventsRepository eventsRepository;

    private final EventService eventService;

    @Override
    public LocationDto createNewLocation(NewLocationDto newLocationDto) {
        Location location = Location.builder()
                .name(newLocationDto.getName())
                .lat(newLocationDto.getLat())
                .lon(newLocationDto.getLon())
                .rad(newLocationDto.getRad())
                .build();

        Location newLocation;

        try {
            newLocation = locationRepository.saveAndFlush(location);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }

        //TODO подумать над тем, как добавляются события, без локации или с
        return LocationMapper.toDto(newLocation, getEventsViewsByLocation(location));
    }

    @Override
    public LocationDto findExactLocation(long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Location with id=%d was not found", locationId)));

        return LocationMapper.toDto(location, getEventsViewsByLocation(location));
    }

    @Override
    public List<LocationDto> findLocations(Float lat, Float lon, Float rad, Boolean closest) {
        List<Location> locations;

        if (lat == null && lon == null)
            locations = locationRepository.findAll();
        else {
            if (rad == null) {
                locations = locationRepository.findByExactCoordinates(lat, lon);
            } else {
                locations = locationRepository.findByCoordinates(lat, lon, rad);

                if (closest)
                    locations = this.findClosestLocation(lat, lon, locations);
            }
        }

        return LocationMapper.toDto(locations, mapEventsViewsByLocation(locations));
    }

    private List<Location> findClosestLocation(Float lat, Float lon, List<Location> locations) {
        Location closestLocation = locations.stream().min((o1, o2) -> {
            Double firstDistance = calculateDistanceBetweenLocations(lat, lon, o1);
            Double secondDistance = calculateDistanceBetweenLocations(lat, lon, o2);

            return firstDistance.compareTo(secondDistance);
        }).get();

        return List.of(closestLocation);
    }

    private Double calculateDistanceBetweenLocations(Float lat, Float lon, Location location) {
        return Math.acos(
                Math.sin(lat) * Math.sin(location.getLat()) +
                Math.cos(lat) * Math.cos(location.getLat()) *
                Math.cos(lon - location.getLon())
        );
    }

    public Map<Long, Long> getEventsViewsByLocation(Location location) {
        return eventService.getEventsViews(location.getEvents());
    }

    public Map<Long, Map<Long, Long>> mapEventsViewsByLocation(List<Location> locations) {
        return locations.stream()
                .collect(Collectors.toMap(Location::getId, this::getEventsViewsByLocation));
    }
}
