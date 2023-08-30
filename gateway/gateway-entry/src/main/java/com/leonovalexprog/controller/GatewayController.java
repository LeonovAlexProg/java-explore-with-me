package com.leonovalexprog.controller;

import com.leonovalexprog.client.EwmClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GatewayController {
    private final EwmClient ewmClient;

    @RequestMapping("/**")
    public ResponseEntity<?> securedEndpoint(HttpServletRequest request, @RequestBody Object requestBody) {
        Mono<Object> mono = ewmClient.someRestCall(request.getRequestURI(), HttpMethod.valueOf(request.getMethod()), requestBody);

        return ResponseEntity.of(mono.blockOptional());
    }
}
