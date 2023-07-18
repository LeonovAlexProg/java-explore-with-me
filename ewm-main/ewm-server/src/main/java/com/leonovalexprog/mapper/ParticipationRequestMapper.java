package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.ParticipationRequestDto;
import com.leonovalexprog.model.ParticipationRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParticipationRequestMapper {
    public static ParticipationRequestDto toDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .requester(participationRequest.getRequester().getId())
                .event(participationRequest.getEvent().getId())
                .status(participationRequest.getStatus())
                .created(participationRequest.getCreated())
                .build();
    }
}
