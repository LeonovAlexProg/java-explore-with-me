package com.leonovalexprog.controller.request;

import com.leonovalexprog.dto.ParticipationRequestDto;
import com.leonovalexprog.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class RequestsPrivateController {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto postParticipationRequest(@PathVariable long userId,
                                                            @RequestParam long eventId) {
        log.info("Register for participation (user id = {}, event id = {})", userId, eventId);
        return requestService.newParticipation(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getParticipationRequest(@PathVariable long userId) {
        log.info("Get user participation requests (user id = {})", userId);
        return requestService.getUserParticipationRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto patchParticipationRequest(@PathVariable long userId,
                                                             @RequestParam long requestId) {
        log.info("Patch user participation request (user id = {}, request id = {})", userId, requestId);
        return requestService.canselParticipationRequest(userId, requestId);
    }
}
