package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.EventDto;
import com.leonovalexprog.model.Event;
import com.leonovalexprog.model.ParticipationRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EventMapper {
    public static EventDto toDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getRequests().stream().filter(request -> request.getStatus().equals(ParticipationRequest.Status.APPROVED)).count())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                //TODO location
                //.location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}
