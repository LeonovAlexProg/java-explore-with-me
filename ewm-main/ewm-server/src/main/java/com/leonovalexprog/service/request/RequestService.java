package com.leonovalexprog.service.request;

import com.leonovalexprog.dto.EventRequestStatusUpdateRequest;
import com.leonovalexprog.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto newParticipation(long userId, long eventId);

    List<ParticipationRequestDto> updateEventRequests(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest);
}
