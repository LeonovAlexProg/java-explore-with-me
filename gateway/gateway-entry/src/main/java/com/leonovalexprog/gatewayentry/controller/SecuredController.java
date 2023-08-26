package com.leonovalexprog.gatewayentry.controller;

import com.leonovalexprog.gatewayentry.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class SecuredController {
    @RequestMapping
    public ResponseEntity<?> securedEndpoint() {
        return ResponseEntity.of(Optional.of("hi"));
    }
}
