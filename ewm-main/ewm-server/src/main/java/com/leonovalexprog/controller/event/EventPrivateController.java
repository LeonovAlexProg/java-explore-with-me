package com.leonovalexprog.controller.event;

import com.leonovalexprog.dto.event.EventDto;
import com.leonovalexprog.dto.event.EventShortDto;
import com.leonovalexprog.dto.event.NewEventDto;
import com.leonovalexprog.dto.event.UpdateEventUserRequest;
import com.leonovalexprog.dto.request.EventRequestStatusUpdateRequest;
import com.leonovalexprog.dto.request.EventRequestStatusUpdateResult;
import com.leonovalexprog.dto.request.ParticipationRequestDto;
import com.leonovalexprog.service.event.EventService;
import com.leonovalexprog.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto postEvent(@PathVariable long userId,
                              @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Add new event (userId = {}, event title = {})", userId, newEventDto.getTitle());
        return eventService.newEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Get user events (user id = {}, from = {}, size = {})", userId, from, size);
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventDto getEvent(@PathVariable long userId,
                             @PathVariable long eventId) {
        log.info("Get user event (user id = {}, event id = {})", userId, eventId);
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventDto patchEvent(@PathVariable long userId,
                                                @PathVariable long eventId,
                                                @Validated @Valid @RequestBody UpdateEventUserRequest newEventDto) {
        log.info("Patch user event (user id = {}, event id = {})", userId, eventId);
        return eventService.updateEvent(userId, eventId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable long userId,
                                                          @PathVariable long eventId) {
        log.info("Get user event requests(user id = {}, event id = {})", userId, eventId);
        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchEventRequests(@PathVariable long userId,
                                                             @PathVariable long eventId,
                                                             @Valid @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        log.info("Update event participation requests (user id = {}, event id = {}, requests = {}",
                userId,
                eventId,
                updateRequest.getRequestIds());
        return requestService.updateEventRequests(userId, eventId, updateRequest);
    }
}
