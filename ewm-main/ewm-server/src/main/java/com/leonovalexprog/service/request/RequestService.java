package com.leonovalexprog.service.request;

import com.leonovalexprog.dto.ParticipationRequestDto;

public interface RequestService {

    ParticipationRequestDto newParticipation(long userId, long eventId);
}
