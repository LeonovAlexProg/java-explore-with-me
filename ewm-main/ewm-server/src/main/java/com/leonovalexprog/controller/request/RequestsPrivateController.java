package com.leonovalexprog.controller.request;

import com.leonovalexprog.dto.ParticipationRequestDto;
import com.leonovalexprog.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

}
