package com.leonovalexprog.service.location;

import com.leonovalexprog.dto.location.LocationDto;
import com.leonovalexprog.dto.location.NewLocationDto;
import com.leonovalexprog.dto.location.UpdateLocationDto;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.FieldValueExistsException;
import com.leonovalexprog.mapper.LocationMapper;
import com.leonovalexprog.model.Event;
import com.leonovalexprog.model.Location;
import com.leonovalexprog.repository.EventsRepository;
import com.leonovalexprog.repository.LocationRepository;
import com.leonovalexprog.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
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
    @Transactional
    public LocationDto createNewLocation(NewLocationDto newLocationDto) {
        Location location = Location.builder()
                .name(newLocationDto.getName())
                .lat(newLocationDto.getLat())
                .lon(newLocationDto.getLon())
                .rad(newLocationDto.getRad())
                .build();

        List<Event> eventsInLocation = eventsRepository.findEventsByCoorinates(newLocationDto.getLat(), newLocationDto.getLon(), newLocationDto.getRad());

        try {
            Location newLocation = locationRepository.save(location);

            eventsInLocation.forEach(event -> event.getLocations().add(newLocation));
            newLocation.setEvents(eventsInLocation);

            locationRepository.saveAndFlush(newLocation);
            eventsRepository.saveAllAndFlush(eventsInLocation);

            return LocationMapper.toDto(newLocation, getEventsViewsByLocation(location));
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
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
            if (rad == 0) {
                locations = locationRepository.findByExactCoordinates(lat, lon);
            } else {
                locations = locationRepository.findByCoordinates(lat, lon, rad);

                if (closest)
                    locations = this.findClosestLocation(lat, lon, locations);
            }
        }

        return LocationMapper.toDto(locations, mapEventsViewsByLocation(locations));
    }

    @Override
    public LocationDto updateLocation(Long locationId, UpdateLocationDto updateLocationDto) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Location with id=%d was not found", locationId)));

        if (updateLocationDto.getName() != null)
            location.setName(updateLocationDto.getName());
        if (updateLocationDto.getLat() != null)
            location.setLat(updateLocationDto.getLat());
        if (updateLocationDto.getLon() != null)
            location.setLon(updateLocationDto.getLon());
        if (updateLocationDto.getRad() != null)
            location.setRad(updateLocationDto.getRad());

        Location patchedLocation;
        try {
            patchedLocation = locationRepository.saveAndFlush(location);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }

        return LocationMapper.toDto(patchedLocation, getEventsViewsByLocation(location));
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
        float lat1 = lat;
        float lon1 = lon;
        float lat2 = location.getLat();
        float lon2 = location.getLon();

        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0.0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;

            dist = dist * 1.609344;

            return (dist);
        }
    }

    public Map<Long, Long> getEventsViewsByLocation(Location location) {
        if (location.getEvents() != null)
            return eventService.getEventsViews(location.getEvents());
        else
            return Collections.emptyMap();
    }

    public Map<Long, Map<Long, Long>> mapEventsViewsByLocation(List<Location> locations) {
        return locations.stream()
                .collect(Collectors.toMap(Location::getId, this::getEventsViewsByLocation));
    }
}
