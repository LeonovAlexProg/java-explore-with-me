package com.leonovalexprog.gatewayentry.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class GatewayController {
    @RequestMapping
    public ResponseEntity<?> securedEndpoint() {
        return ResponseEntity.of(Optional.of("hi"));
    }
}
