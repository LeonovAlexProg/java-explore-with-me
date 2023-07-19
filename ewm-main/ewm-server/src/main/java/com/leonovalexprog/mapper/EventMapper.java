package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.EventDto;
import com.leonovalexprog.dto.EventShortDto;
import com.leonovalexprog.model.Event;
import com.leonovalexprog.model.ParticipationRequest;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {
    public static EventDto toDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getRequests().stream().filter(request -> request.getStatus().equals(ParticipationRequest.Status.CONFIRMED)).count())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .location(LocationMapper.toDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getRequests().stream().filter(request -> request.getStatus().equals(ParticipationRequest.Status.CONFIRMED)).count())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static List<EventShortDto> toShortDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }
}
