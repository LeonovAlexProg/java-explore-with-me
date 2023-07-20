package com.leonovalexprog.service.event;

import com.leonovalexprog.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventDto newEvent(long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(long userId, int from, int size);

    EventDto getEvent(long userId, long eventId);

    EventDto updateEvent(long userId, long eventId, UpdateEventUserRequest newEventData);

    List<ParticipationRequestDto> getEventRequests(long userId, long eventId);

    List<EventDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventDto updateEventAdmin(long eventId, UpdateEventAdminRequest updateDto);
}
