package com.leonovalexprog.service.request;

import com.leonovalexprog.dto.EventRequestStatusUpdateRequest;
import com.leonovalexprog.dto.EventRequestStatusUpdateResult;
import com.leonovalexprog.dto.ParticipationRequestDto;

public interface RequestService {

    ParticipationRequestDto newParticipation(long userId, long eventId);

    EventRequestStatusUpdateResult updateEventRequests(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest);
}
