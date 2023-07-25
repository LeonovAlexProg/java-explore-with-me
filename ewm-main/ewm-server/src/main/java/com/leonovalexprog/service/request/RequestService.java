package com.leonovalexprog.service.request;

import com.leonovalexprog.dto.EventRequestStatusUpdateRequest;
import com.leonovalexprog.dto.EventRequestStatusUpdateResult;
import com.leonovalexprog.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto newParticipation(long userId, long eventId);

    EventRequestStatusUpdateResult updateEventRequests(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest);

    List<ParticipationRequestDto> getUserParticipationRequests(long userId);

    ParticipationRequestDto canselParticipationRequest(long userId, long requestId);
}
