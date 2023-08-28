package com.leonovalexprog.controller.event;

import com.leonovalexprog.client.StatsClient;
import com.leonovalexprog.dto.event.EventDto;
import com.leonovalexprog.dto.event.EventShortDto;
import com.leonovalexprog.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(defaultValue = "") String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(required = false) boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest httpServletRequest) {
        log.info("Get events by public (text = {}, categories = {}, paid = {}, rangeStart = {}, rangeEnd = {}, " +
                "onlyAvailable = {}, sort = {}, from = {}, size = {})",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        statsClient.registerRequest("ewm-main-service", httpServletRequest);

        return eventService.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable(value = "id") Long eventId, HttpServletRequest httpServletRequest) {
        log.info("Get event by public (event id = {})", eventId);

        statsClient.registerRequest("ewm-main-service", httpServletRequest);

        return eventService.getEventByPublic(eventId);
    }
}
