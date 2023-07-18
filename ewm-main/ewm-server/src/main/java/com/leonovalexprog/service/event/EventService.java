package com.leonovalexprog.service.event;

import com.leonovalexprog.dto.EventDto;
import com.leonovalexprog.dto.NewEventDto;

public interface EventService {

    EventDto newEvent(long userId, NewEventDto newEventDto);
}
