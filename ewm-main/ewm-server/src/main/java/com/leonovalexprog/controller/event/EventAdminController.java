package com.leonovalexprog.controller.event;

import com.leonovalexprog.dto.EventDto;
import com.leonovalexprog.dto.UpdateEventAdminRequest;
import com.leonovalexprog.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getEvents(@RequestParam(required = false) List<Long> users,
                              @RequestParam(required = false) List<String> states,
                              @RequestParam(required = false)  List<Long> categories,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                              @RequestParam(defaultValue = "0") int from,
                              @RequestParam(defaultValue = "10") int size) {
        log.info("Get events by admin (users = {}, states = {}, categories = {}, rangeStart = {}, rangeEnd = {}, from = {}, size = {}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventDto patchEvent(@PathVariable long eventId,
                               @RequestBody UpdateEventAdminRequest updateDto) {
        log.info("Update event (event id = {}, state action = {})", eventId, updateDto.getStateAction());

        return eventService.updateEventAdmin(eventId, updateDto);
    }
}
