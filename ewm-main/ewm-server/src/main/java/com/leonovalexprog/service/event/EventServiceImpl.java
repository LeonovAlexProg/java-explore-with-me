package com.leonovalexprog.service.event;

import com.leonovalexprog.client.StatsClient;
import com.leonovalexprog.dto.RequestResponseDto;
import com.leonovalexprog.dto.event.*;
import com.leonovalexprog.dto.request.ParticipationRequestDto;
import com.leonovalexprog.exception.exceptions.*;
import com.leonovalexprog.mapper.EventMapper;
import com.leonovalexprog.mapper.ParticipationRequestMapper;
import com.leonovalexprog.model.*;
import com.leonovalexprog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoryRepository;
    private final EventLocationRepository eventLocationRepository;
    private final LocationRepository locationRepository;

    private final StatsClient statsClient;

    @Override
    @Transactional
    public EventDto newEvent(long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataValidationFailException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " +
                    newEventDto.getEventDate());
        }

        User eventInitiator = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User with id=%d was not found", userId)));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new EntityNotExistsException(String.format("Category with id=%d was not found", newEventDto.getCategory())));

        EventLocation eventLocation = EventLocation.builder()
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .build();
        EventLocation newEventLocation = eventLocationRepository.save(eventLocation);

        List<Location> locations = locationRepository.findByEventCoordinates(newEventLocation.getLat(), newEventLocation.getLon());

        Event event = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .requests(new ArrayList<>())
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(eventInitiator)
                .eventLocation(newEventLocation)
                .locations(locations)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration())
                .state(Event.State.PENDING)
                .title(newEventDto.getTitle())
                .build();

        try {
            Event newEvent = eventsRepository.saveAndFlush(event);

            newEventLocation.setEvent(newEvent);
            eventLocationRepository.saveAndFlush(newEventLocation);

            locations.forEach(location -> location.getEvents().add(newEvent));
            locationRepository.saveAllAndFlush(locations);

            return EventMapper.toDto(newEvent, getEventViews(newEvent));
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }

    @Override
    public List<EventShortDto> getEvents(long userId, int from, int size) {
        if (!userRepository.existsById(userId))
            throw new EntityNotExistsException(String.format("User with id=%d was not found", userId));

        List<Event> userEvents = eventsRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size));

        return EventMapper.toShortDto(userEvents, getEventsViews(userEvents));
    }

    @Override
    public EventDto getEvent(long userId, long eventId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotExistsException(String.format("User with id=%d was not found", userId));

        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));

        return EventMapper.toDto(event, getEventViews(event));
    }

    @Override
    @Transactional
    public EventDto updateEvent(long userId, long eventId, UpdateEventUserRequest newEventDto) {
        if (newEventDto.getEventDate() != null) {
            if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DataValidationFailException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " +
                        newEventDto.getEventDate());
            }
        }


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User with id=%d was not found", userId)));
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));

        if (!event.getInitiator().getId().equals(user.getId()))
            throw new ConditionsViolationException("Only event initiator can update this event");
        if (!event.getState().equals(Event.State.PENDING) && !event.getState().equals(Event.State.CANCELED))
            throw new ConditionsViolationException("Only pending or canceled events can be changed");

        if (newEventDto.getAnnotation() != null)
            event.setAnnotation(newEventDto.getAnnotation());

        if (newEventDto.getCategory() != null) {
            Category category = categoryRepository.findById(newEventDto.getCategory().getId())
                    .orElseThrow(() -> new EntityNotExistsException(String.format("Category with id=%d was not found", newEventDto.getCategory().getId())));
            event.setCategory(category);
        }

        if (newEventDto.getDescription() != null)
            event.setDescription(newEventDto.getDescription());
        if (newEventDto.getEventDate() != null)
            event.setEventDate(newEventDto.getEventDate());

        if (newEventDto.getLocation() != null) {
            if (!eventLocationRepository.existsByLatAndLon(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon())) {
                EventLocation eventLocation = EventLocation.builder()
                        .lat(newEventDto.getLocation().getLat())
                        .lon(newEventDto.getLocation().getLon())
                        .event(event)
                        .build();
                EventLocation newEventLocation = eventLocationRepository.save(eventLocation);

                event.setEventLocation(newEventLocation);
            }
        }

        if (newEventDto.getPaid() != null)
            event.setPaid(newEventDto.getPaid());
        if (newEventDto.getParticipantLimit() != null)
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        if (newEventDto.getRequestModeration() != null)
            event.setRequestModeration(newEventDto.getRequestModeration());

        if (newEventDto.getStateAction() != null) {
            if (newEventDto.getStateAction().equals(UpdateEventUserRequest.StateAction.CANCEL_REVIEW))
                event.setState(Event.State.CANCELED);
            else
                event.setState(Event.State.PENDING);
        }

        if (newEventDto.getTitle() != null)
            event.setTitle(newEventDto.getTitle());

        try {
            Event newEvent = eventsRepository.saveAndFlush(event);
            return EventMapper.toDto(newEvent, getEventViews(newEvent));
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
        List<Event.State> searchingStates;

        Pageable pageable = PageRequest.of(from / size, size);

        if (states != null) {
            searchingStates = Arrays.stream(Event.State.values())
                    .filter(e -> states.contains(e.toString()))
                    .collect(Collectors.toList());
        } else {
            searchingStates = Arrays.stream(Event.State.values())
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

        return EventMapper.toDto(events, getEventsViews(events));
    }

    @Override
    @Transactional
    public EventDto updateEventAdmin(long eventId, UpdateEventAdminRequest updateDto) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));

        if (!event.getState().equals(Event.State.PENDING) && !event.getState().equals(Event.State.CANCELED))
            throw new ConditionsViolationException("Only pending or canceled events can be changed");
        if (updateDto.getStateAction() != null) {
            if (updateDto.getStateAction().equals(UpdateEventAdminRequest.StateAction.PUBLISH_EVENT) && event.getState().equals(Event.State.CANCELED))
                throw new ConditionsViolationException("Cancelled event can't be published");
        }

        if (updateDto.getAnnotation() != null)
            event.setAnnotation(updateDto.getAnnotation());
        if (updateDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateDto.getCategory())
                    .orElseThrow(() -> new EntityNotExistsException(String.format("Category with id=%d was not found", updateDto.getCategory())));

            event.setCategory(category);
        }
        if (updateDto.getDescription() != null)
            event.setDescription(updateDto.getDescription());
        if (updateDto.getEventDate() != null) {
            if (updateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " +
                        updateDto.getEventDate());
            }

            event.setEventDate(updateDto.getEventDate());
        }
        if (updateDto.getLocation() != null) {
            EventLocation eventLocation = eventLocationRepository.findByLatAndLon(updateDto.getLocation().getLat(), updateDto.getLocation().getLon());
            if (eventLocation == null) {
                EventLocation newEventLocation = EventLocation.builder()
                        .lat(updateDto.getLocation().getLat())
                        .lon(updateDto.getLocation().getLon())
                        .build();

                newEventLocation = eventLocationRepository.save(newEventLocation);

                event.setEventLocation(newEventLocation);
            } else {
                event.setEventLocation(eventLocation);
            }
        }
        if (updateDto.getPaid() != null)
            event.setPaid(updateDto.getPaid());
        if (updateDto.getParticipantLimit() != null)
            event.setParticipantLimit(updateDto.getParticipantLimit());
        if (updateDto.getRequestModeration() != null)
            event.setRequestModeration(updateDto.getRequestModeration());
        if (updateDto.getStateAction() != null) {
            if (updateDto.getStateAction().equals(UpdateEventAdminRequest.StateAction.PUBLISH_EVENT)) {
                event.setState(Event.State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                event.setState(Event.State.CANCELED);
            }
        }
        if (updateDto.getTitle() != null)
            event.setTitle(updateDto.getTitle());

        try {
            Event newEvent = eventsRepository.saveAndFlush(event);
            return EventMapper.toDto(newEvent,getEventViews(newEvent));
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }

    @Override
    public List<EventShortDto> getEventsByPublic(String text, List<Long> categories, Boolean paid,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        String textFilter;
        List<Category> categoriesFilter;
        List<Boolean> paidFilter;
        LocalDateTime rangeStartFilter;
        LocalDateTime rangeEndFilter;
        Pageable pageable;

        textFilter = text.toLowerCase();

        if (categories != null)
            categoriesFilter = categoryRepository.findAllById(categories);
        else
            categoriesFilter = categoryRepository.findAll();

        if (paid != null)
            paidFilter = List.of(paid);
        else
            paidFilter = List.of(Boolean.TRUE, Boolean.FALSE);

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd))
                throw new BadRequestException("Range end is before Range start");

            rangeStartFilter = rangeStart;
            rangeEndFilter = rangeEnd;
        } else {
            rangeStartFilter = LocalDateTime.now();
            rangeEndFilter = rangeStartFilter.plusYears(1000);
        }

        if (sort == null || (!sort.equals("EVENT_DATE") && !sort.equals("VIEWS")))
            pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        else {
            if (sort.equals("EVENT_DATE")) {
                pageable = PageRequest.of(from / size, size, Sort.by("eventDate").ascending());
            } else {
                pageable = PageRequest.of(from / size, size, Sort.by("views").ascending());
            }
        }

        List<Event> events;

        if (onlyAvailable)
            events = eventsRepository.findPublicAvailable(textFilter, categoriesFilter, paidFilter, rangeStartFilter, rangeEndFilter, pageable);
        else
            events = eventsRepository.findPublic(textFilter, categoriesFilter, paidFilter, rangeStartFilter, rangeEndFilter, pageable);

        return EventMapper.toShortDto(events, getEventsViews(events));
    }

    @Override
    public EventDto getEventByPublic(Long eventId) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));

        if (!event.getState().equals(Event.State.PUBLISHED))
            throw new EntityNotExistsException("Event must be published");

        try {
            eventsRepository.saveAndFlush(event);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }

        return EventMapper.toDto(event, getEventViews(event));
    }

    public Long getEventViews(Event event) {
        if (event.getId() != null) {
            List<RequestResponseDto> eventRequests = statsClient.getRequestsStat(
                    LocalDateTime.now().minusYears(100),
                    LocalDateTime.now().plusYears(100),
                    List.of("/events/" + event.getId()),
                    true);

            if (!eventRequests.isEmpty())
                return (long) eventRequests.get(0).getHits();
        }

        return 0L;
    }

    public Map<Long, Long> getEventsViews(List<Event> events) {
        List<String> eventsUri = events.stream()
                .map(e -> "/events/" + e.getId())
                .collect(Collectors.toList());

        List<RequestResponseDto> eventsRequests = statsClient.getRequestsStat(
                LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                eventsUri,
                true);

        if (!events.isEmpty()) {
            return eventsRequests.stream()
                    .collect(Collectors.toMap(r -> Long.valueOf(r.getUri().substring(8)), r -> (long) r.getHits()));
        } else {
            return Collections.emptyMap();
        }
    }
}
