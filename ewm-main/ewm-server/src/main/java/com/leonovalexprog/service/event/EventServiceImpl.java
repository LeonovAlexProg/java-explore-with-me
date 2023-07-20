package com.leonovalexprog.service.event;

import com.leonovalexprog.dto.*;
import com.leonovalexprog.exception.exceptions.ConditionsViolationException;
import com.leonovalexprog.exception.exceptions.DataValidationFailException;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.FieldValueExistsException;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            throw new FieldValueExistsException(exception.getMessage());
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


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User with id=%d was not found", userId)));
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));
        Category category = categoryRepository.findById(newEventDto.getCategory().getId())
                .orElseThrow(() -> new EntityNotExistsException(String.format("Category with id=%d was not found", newEventDto.getCategory().getId())));

        if (!event.getInitiator().getId().equals(user.getId()))
            throw new ConditionsViolationException("Only event initiator can update this event");
        if (event.getState().equals(Event.State.PENDING) || event.getState().equals(Event.State.CANCELED))
            throw new ConditionsViolationException("Only pending or canceled events can be changed");

        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        if (!locationRepository.existsByLatAndLon(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon())) {
            Location location = Location.builder()
                    .lat(newEventDto.getLocation().getLat())
                    .lon(newEventDto.getLocation().getLon())
                    .build();
            Location newLocation = locationRepository.save(location);

            event.setLocation(newLocation);
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
            throw new FieldValueExistsException(exception.getMessage());
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

    @Override
    public List<EventDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<Event> events;
        List<String> searchingStates;

        Pageable pageable = PageRequest.of(from / size, size);

        if (categories != null) {
            searchingStates = Arrays.stream(Event.State.values())
                    .map(Enum::toString)
                    .filter(states::contains)
                    .collect(Collectors.toList());
        } else {
            searchingStates = Arrays.stream(Event.State.values())
                    .map(Enum::toString)
                    .collect(Collectors.toList());
        }

        if (users != null && categories != null) {
            if (rangeStart != null && rangeEnd != null) {
                events = eventsRepository.findByUsersAndCategoriesWithTimestamp(users, categories, searchingStates, rangeStart, rangeEnd, pageable);
            } else {
                events = eventsRepository.findByUsersAndCategories(users, categories, searchingStates, pageable);
            }
        } else if (users != null) {
            if (rangeStart != null && rangeEnd != null) {
                events = eventsRepository.findByUsersWithTimestamp(users, searchingStates, rangeStart, rangeEnd, pageable);
            } else {
                events = eventsRepository.findByUsers(users, searchingStates, pageable);
            }
        } else if (categories != null) {
            if (rangeStart != null && rangeEnd != null) {
                events = eventsRepository.findByCategoriesWithTimestamp(categories, searchingStates, rangeStart, rangeEnd, pageable);
            } else {
                events = eventsRepository.findByCategories(categories, searchingStates, pageable);
            }
        } else {
            if (rangeStart != null && rangeEnd != null) {
                events = eventsRepository.findWithTimestamp(searchingStates, rangeStart, rangeEnd, pageable);
            } else {
                events = eventsRepository.findByStates(searchingStates, pageable);
            }
        }

        return EventMapper.toDto(events);
    }

    @Override
    public EventDto updateEventAdmin(long eventId, UpdateEventAdminRequest updateDto) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));

        if (event.getState().equals(Event.State.PENDING) || event.getState().equals(Event.State.CANCELED))
            throw new ConditionsViolationException("Only pending or canceled events can be changed");

        if (updateDto.getAnnotation() != null)
            event.setAnnotation(updateDto.getAnnotation());
        if (updateDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateDto.getCategory().getId())
                    .orElseThrow(() -> new EntityNotExistsException(String.format("Category with id=%d was not found", updateDto.getCategory().getId())));

            event.setCategory(category);
        }
        if (updateDto.getDescription() != null)
            event.setDescription(updateDto.getDescription());
        if (updateDto.getEventDate() != null) {
            if (updateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DataValidationFailException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " +
                        updateDto.getEventDate());
            }

            event.setEventDate(updateDto.getEventDate());
        }
        if (updateDto.getLocation() != null) {
            Location location = locationRepository.findByLatAndLon(updateDto.getLocation().getLat(), updateDto.getLocation().getLon());

            event.setLocation(location);
        }
        if (updateDto.getPaid() != null)
            event.setPaid(updateDto.getPaid());
        if (updateDto.getParticipantLimit() != null)
            event.setParticipantLimit(updateDto.getParticipantLimit());
        if (updateDto.getRequestModeration() != null)
            event.setRequestModeration(updateDto.getRequestModeration());
        if (updateDto.getStateAction() != null) {
            if (updateDto.getStateAction().equals(UpdateEventAdminRequest.StateAction.PUBLISH_EVENT))
                event.setState(Event.State.PUBLISHED);
            else
                event.setState(Event.State.CANCELED);
        }
        if (updateDto.getTitle() != null)
            event.setTitle(updateDto.getTitle());

        try {
            Event newEvent = eventsRepository.saveAndFlush(event);
            return EventMapper.toDto(newEvent);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }
}
