package com.leonovalexprog.service.request;

import com.leonovalexprog.dto.request.EventRequestStatusUpdateRequest;
import com.leonovalexprog.dto.request.EventRequestStatusUpdateResult;
import com.leonovalexprog.dto.request.ParticipationRequestDto;
import com.leonovalexprog.exception.exceptions.ConditionsViolationException;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.FieldValueExistsException;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final ParticipationRequestsRepository requestRepository;
    private final EventsRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ParticipationRequestDto newParticipation(long userId, long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User with id=%d was not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));

        if (!event.getState().equals(Event.State.PUBLISHED))
            throw new ConditionsViolationException("Event is not published");

        if (event.getRequests().stream()
                .map(ParticipationRequest::getRequester)
                .anyMatch(requester -> requester.equals(user))) {
            throw new ConditionsViolationException("User already registered for participation in event");
        }
        if (event.getInitiator().equals(user))
            throw new ConditionsViolationException("User is initiator of this event");
        if (!event.getParticipantLimit().equals(0L)) {
            List<ParticipationRequest> approvedRequests = event.getRequests().stream()
                    .filter(r -> r.getStatus().equals(ParticipationRequest.Status.CONFIRMED))
                    .collect(Collectors.toList());

            if (approvedRequests.size() == event.getParticipantLimit())
                throw new ConditionsViolationException("Participation limit is overflowed");
        }

        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .requester(user)
                .created(LocalDateTime.now())
                .status(ParticipationRequest.Status.PENDING)
                .event(event)
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0L))
            participationRequest.setStatus(ParticipationRequest.Status.CONFIRMED);

        try {
            ParticipationRequest newParticipationRequest = requestRepository.saveAndFlush(participationRequest);
            return ParticipationRequestMapper.toDto(newParticipationRequest);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequests(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User with id=%d was not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Event with id=%d was not found", eventId)));

        if (!event.getInitiator().getId().equals(user.getId()))
            throw new ConditionsViolationException("Only event initiator can update this event");

        long approvedRequests = requestRepository.findAllByEventIdAndStatus(event.getId(), ParticipationRequest.Status.CONFIRMED).size();
        if (event.getParticipantLimit().equals(approvedRequests))
            throw new ConditionsViolationException("Event's participant limit is full");

        List<ParticipationRequest> eventRequests = event.getRequests();
        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        eventRequests.forEach(request -> {
                    if (event.getParticipantLimit().equals(
                          eventRequests.stream()
                                  .filter(e -> e.getStatus().equals(ParticipationRequest.Status.CONFIRMED))
                                  .count()
                    )) {
                        return;
                    }

                    if (updateRequest.getRequestIds().contains(request.getId())) {
                        if (request.getStatus().equals(ParticipationRequest.Status.PENDING)) {
                            request.setStatus(updateRequest.getStatus().equals(
                                    EventRequestStatusUpdateRequest.Status.CONFIRMED) ? ParticipationRequest.Status.CONFIRMED : ParticipationRequest.Status.REJECTED
                            );

                            if (request.getStatus().equals(ParticipationRequest.Status.CONFIRMED))
                                updateResult.getConfirmedRequests().add(ParticipationRequestMapper.toDto(request));
                            else
                                updateResult.getRejectedRequests().add(ParticipationRequestMapper.toDto(request));
                        } else {
                            throw new ConditionsViolationException(String.format("Participation id=%d status is not pending", request.getId()));
                        }
                    }
                });

        approvedRequests = requestRepository.findAllByEventIdAndStatus(event.getId(), ParticipationRequest.Status.CONFIRMED).size();
        if (event.getParticipantLimit().equals(approvedRequests)) {
            eventRequests.forEach(request -> {
                        if (request.getStatus().equals(ParticipationRequest.Status.PENDING)) {
                            request.setStatus(ParticipationRequest.Status.REJECTED);

                            updateResult.getRejectedRequests().add(ParticipationRequestMapper.toDto(request));
                        }
                    });
        }

        try {
            eventRepository.saveAndFlush(event);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }

        return updateResult;
    }

    @Override
    public List<ParticipationRequestDto> getUserParticipationRequests(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User with id=%d was not found", userId)));

        List<ParticipationRequest> requests = requestRepository.findAllByUser(user);

        return ParticipationRequestMapper.toDto(requests);
    }

    @Override
    public ParticipationRequestDto canselParticipationRequest(long userId, long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User with id=%d was not found", userId)));
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Participation request with id=%d was not found", requestId)));

        requestRepository.delete(request);

        request.setStatus(ParticipationRequest.Status.CANCELED);

        return ParticipationRequestMapper.toDto(request);
    }
}
