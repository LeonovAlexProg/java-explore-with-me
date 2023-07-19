package com.leonovalexprog.service.event;

import com.leonovalexprog.dto.EventDto;
import com.leonovalexprog.dto.EventShortDto;
import com.leonovalexprog.dto.NewEventDto;
import com.leonovalexprog.exception.exceptions.DataValidationFailException;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.NameExistsException;
import com.leonovalexprog.mapper.EventMapper;
import com.leonovalexprog.mapper.LocationMapper;
import com.leonovalexprog.model.Category;
import com.leonovalexprog.model.Event;
import com.leonovalexprog.model.Location;
import com.leonovalexprog.model.User;
import com.leonovalexprog.repository.CategoriesRepository;
import com.leonovalexprog.repository.EventsRepository;
import com.leonovalexprog.repository.LocationRepository;
import com.leonovalexprog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    public List<EventShortDto> getEvents(long userId, long from, long size) {
        return null;
    }
}
