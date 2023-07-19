package com.leonovalexprog.service.event;

import com.leonovalexprog.dto.EventDto;
import com.leonovalexprog.dto.EventShortDto;
import com.leonovalexprog.dto.NewEventDto;

import java.util.List;

public interface EventService {

    EventDto newEvent(long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(long userId, long from, long size);
}
