package com.leonovalexprog.service.event;

import com.leonovalexprog.dto.*;
import com.leonovalexprog.exception.exceptions.ConditionsViolationException;
import com.leonovalexprog.exception.exceptions.DataValidationFailException;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.NameExistsException;
import com.leonovalexprog.mapper.EventMapper;
import com.leonovalexprog.mapper.ParticipationRequestMapper;
import com.leonovalexprog.model.*;
import com.leonovalexprog.repository.CategoriesRepository;
import com.leonovalexprog.repository.EventsRepository;
import com.leonovalexprog.repository.LocationRepository;
import com.leonovalexprog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Override
    public EventDto newEvent(long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataValidationFailException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " +
                    newEventDto.getEventDate());
        }

        User eventInitiator = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User with id=%d was not found", userId)));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new EntityNotExistsException(String.format("Category with id=%d was not found", newEventDto.getCategory())));

        Location location = Location.builder()
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .build();

        Location newLocation = locationRepository.save(location);


        Event event = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .requests(new ArrayList<>())
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(eventInitiator)
                .location(newLocation)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration())
                .state(Event.State.PENDING)
                .title(newEventDto.getTitle())
                .views(0L)
                .build();

        try {
            Event newEvent = eventsRepository.saveAndFlush(event);
            return EventMapper.toDto(newEvent);
        } catch (DataIntegrityViolationException exception) {
            throw new NameExistsException(exception.getMessage());
        }
    }

    @Override
    public List<EventShortDto> getEvents(long userId, int from, int size) {
        if (!userRepository.existsById(userId))
            throw new EntityNotExistsException(String.format("User with id=%d was not found", userId));

        List<Event> userEvents = eventsRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size));

        return EventMapper.toShortDto(userEvents);
    }

    @Override
    public EventDto getEvent(long userId, long eventId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotExistsException(String.format("User with id=%d was not found", userId));

        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));

        return EventMapper.toDto(event);
    }

    @Override
    public EventDto updateEvent(long userId, long eventId, UpdateEventUserRequest newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataValidationFailException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " +
                    newEventDto.getEventDate());
        }

        if (!userRepository.existsById(userId))
            throw new EntityNotExistsException(String.format("User with id=%d was not found", userId));

        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));
        Category category = categoryRepository.findById(newEventDto.getCategory().getId())
                .orElseThrow(() -> new EntityNotExistsException(String.format("Category with id=%d was not found", newEventDto.getCategory().getId())));

        if (event.getState().equals(Event.State.PENDING) || event.getState().equals(Event.State.CANCELED))
            throw new ConditionsViolationException("Only pending or canceled events can be changed");

        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        if (!locationRepository.existsByLatAndLon(newEventDto.getLocationDto().getLat(), newEventDto.getLocationDto().getLon())) {
            Location location = Location.builder()
                    .lat(newEventDto.getLocationDto().getLat())
                    .lon(newEventDto.getLocationDto().getLon())
                    .build();
            Location newLocation = locationRepository.save(location);

            event.setLocation(location);
        }
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setRequestModeration(newEventDto.getRequestModeration());
        if (newEventDto.getStateAction().equals(UpdateEventUserRequest.StateAction.CANSEL_REVIEW))
            event.setState(Event.State.CANCELED);
        else
            event.setState(Event.State.PENDING);
        event.setTitle(newEventDto.getTitle());

        try {
            Event newEvent = eventsRepository.saveAndFlush(event);
            return EventMapper.toDto(newEvent);
        } catch (DataIntegrityViolationException exception) {
            throw new NameExistsException(exception.getMessage());
        }
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(long userId, long eventId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotExistsException(String.format("User with id=%d was not found", userId));
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));

        List<ParticipationRequest> eventRequests = event.getRequests();

        return ParticipationRequestMapper.toDto(eventRequests);
    }


}
