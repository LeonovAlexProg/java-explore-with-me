package com.leonovalexprog.gatewayentry.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class GatewayController {
    @GetMapping("/admin")
    public ResponseEntity<?> securedEndpoint() {
        return ResponseEntity.of(Optional.of("hi"));
    }

    @GetMapping("/users")
    public ResponseEntity<?> secondSecuredEndpoint() {
        return ResponseEntity.of(Optional.of("bye"));
    }
}
