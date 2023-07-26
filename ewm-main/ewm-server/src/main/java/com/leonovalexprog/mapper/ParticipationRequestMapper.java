package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.request.ParticipationRequestDto;
import com.leonovalexprog.model.ParticipationRequest;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<ParticipationRequestDto> toDto(List<ParticipationRequest> participationRequestList) {
        return participationRequestList.stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
