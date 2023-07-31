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
            sortEventsByClosest(eventsInLocation, newLocation);
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
        sortEventsByClosest(location.getEvents(), location);

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

        locations.forEach(l -> sortEventsByClosest(l.getEvents(), l));

        return LocationMapper.toDto(locations, mapEventsViewsByLocation(locations));
    }

    @Override
    public LocationDto  updateLocation(Long locationId, UpdateLocationDto updateLocationDto) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Location with id=%d was not found", locationId)));

        boolean isCoordinatesUpdated = false;

        if (updateLocationDto.getName() != null) {
            location.setName(updateLocationDto.getName());
        }
        if (updateLocationDto.getLat() != null) {
            location.setLat(updateLocationDto.getLat());
            isCoordinatesUpdated = true;
        }
        if (updateLocationDto.getLon() != null) {
            location.setLon(updateLocationDto.getLon());
            isCoordinatesUpdated = true;
        }
        if (updateLocationDto.getRad() != null) {
            location.setRad(updateLocationDto.getRad());
            isCoordinatesUpdated = true;
        }

        if (isCoordinatesUpdated) {
            List<Event> oldEventsInLocation = location.getEvents();
            oldEventsInLocation.forEach(event -> event.setLocations(locationRepository.findByExactCoordinates(
                            event.getEventLocation().getLat(),
                            event.getEventLocation().getLon()
                    )));
            eventsRepository.saveAll(oldEventsInLocation);

            List<Event> newEventsInLocation = eventsRepository.findEventsByCoorinates(location.getLat(), location.getLon(), location.getRad());
            location.setEvents(newEventsInLocation);
        }

        try {
            Location patchedLocation = locationRepository.saveAndFlush(location);
        return LocationMapper.toDto(patchedLocation, getEventsViewsByLocation(location));
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }

    private void sortEventsByClosest(List<Event> events, Location location) {
        events.sort((Event e1, Event e2) -> {
            Double firstDistance = calculateDistanceBetweenCoordinates(
                    e1.getEventLocation().getLat(), e1.getEventLocation().getLon(),
                    location.getLat(), location.getLon()
            );
            Double secondDistance = calculateDistanceBetweenCoordinates(
                    e2.getEventLocation().getLat(), e2.getEventLocation().getLon(),
                    location.getLat(), location.getLon()
            );

            return firstDistance.compareTo(secondDistance);
        });
    }

    private List<Location> findClosestLocation(Float lat, Float lon, List<Location> locations) {
        Location closestLocation = locations.stream().min((o1, o2) -> {
            Double firstDistance = calculateDistanceBetweenCoordinates(lat, lon, o1.getLat(), o1.getLon());
            Double secondDistance = calculateDistanceBetweenCoordinates(lat, lon, o2.getLat(), o2.getLon());

            return firstDistance.compareTo(secondDistance);
        }).get();

        return List.of(closestLocation);
    }

    private Double calculateDistanceBetweenCoordinates(float lat1, float lon1, float lat2, float lon2) {

        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0.0;
        } else {
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
