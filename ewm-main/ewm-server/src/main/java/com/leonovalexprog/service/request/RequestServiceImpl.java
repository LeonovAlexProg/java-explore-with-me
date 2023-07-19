package com.leonovalexprog.service.request;

import com.leonovalexprog.dto.ParticipationRequestDto;
import com.leonovalexprog.exception.exceptions.ConditionsViolationException;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.NameExistsException;
import com.leonovalexprog.mapper.ParticipationRequestMapper;
import com.leonovalexprog.model.Event;
import com.leonovalexprog.model.ParticipationRequest;
import com.leonovalexprog.model.User;
import com.leonovalexprog.repository.EventsRepository;
import com.leonovalexprog.repository.ParticipationRequestsRepository;
import com.leonovalexprog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final ParticipationRequestsRepository requestRepository;
    private final EventsRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ParticipationRequestDto newParticipation(long userId, long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User with id=%d was not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));

        if (event.getRequests().stream()
                .map(ParticipationRequest::getRequester)
                .anyMatch(requester -> requester.equals(user))) {
            throw new ConditionsViolationException("User already registered for participation in event");
        }
        if (event.getInitiator().equals(user))
            throw new ConditionsViolationException("User is initiator of this event");
        if (event.getRequests().size() == event.getParticipantLimit())
            throw new ConditionsViolationException("Participation limit is overflowed");

        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .requester(user)
                .created(LocalDateTime.now())
                .status(ParticipationRequest.Status.PENDING)
                .event(event)
                .build();

        if (!event.getRequestModeration())
            participationRequest.setStatus(ParticipationRequest.Status.APPROVED);

        try {
            ParticipationRequest newParticipationRequest = requestRepository.saveAndFlush(participationRequest);
            return ParticipationRequestMapper.toDto(newParticipationRequest);
        } catch (DataIntegrityViolationException exception) {
            throw new NameExistsException(exception.getMessage());
        }
    }
}
