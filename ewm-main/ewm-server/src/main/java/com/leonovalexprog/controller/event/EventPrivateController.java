package com.leonovalexprog.controller.event;

import com.leonovalexprog.dto.EventDto;
import com.leonovalexprog.dto.NewEventDto;
import com.leonovalexprog.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping("/{userId}/events")
    public EventDto postEvent(@PathVariable long userId,
                              @RequestBody NewEventDto newEventDto) {
        log.info("Add new event (userId = {}, event title = {})", userId, newEventDto.getTitle());
        return eventService.newEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events")
    public EventDto getEvents(@PathVariable long userId,
                              @RequestParam(defaultValue = "0") long from,
                              @RequestParam(defaultValue = "10") long size) {
        log.info("Get user events (user id = {}, from = {}, size = {})", userId, from, size);
        return eventService.getEvents(userId, from, size);
    }
}
