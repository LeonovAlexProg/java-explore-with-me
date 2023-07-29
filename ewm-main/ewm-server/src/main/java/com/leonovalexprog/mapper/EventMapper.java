package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.event.EventDto;
import com.leonovalexprog.dto.event.EventShortDto;
import com.leonovalexprog.model.Event;
import com.leonovalexprog.model.ParticipationRequest;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {
    public static EventDto toDto(Event event, Long views) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getRequests().stream().filter(request -> request.getStatus().equals(ParticipationRequest.Status.CONFIRMED)).count())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .location(EventLocationMapper.toDto(event.getEventLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static List<EventDto> toDto(List<Event> events, Map<Long, Long> views) {
        return events.stream()
                .map(event -> EventMapper.toDto(event, views.get(event.getId())))
                .collect(Collectors.toList());
    }

    public static EventShortDto toShortDto(Event event, Long views) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getRequests().stream().filter(request -> request.getStatus().equals(ParticipationRequest.Status.CONFIRMED)).count())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static List<EventShortDto> toShortDto(List<Event> events, Map<Long, Long> views) {
        return events.stream()
                .map(event -> EventMapper.toShortDto(event, views.get(event.getId())))
                .collect(Collectors.toList());
    }
}
