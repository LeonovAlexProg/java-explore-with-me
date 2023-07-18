package com.leonovalexprog.service.event;

import com.leonovalexprog.dto.EventDto;
import com.leonovalexprog.dto.NewEventDto;
import com.leonovalexprog.repository.EventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventsRepository eventsRepository;

    @Override
    public EventDto newEvent(long userId, NewEventDto newEventDto) {
        return null;
    }
}
