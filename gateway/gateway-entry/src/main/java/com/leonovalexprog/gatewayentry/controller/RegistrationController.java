package com.leonovalexprog.gatewayentry.controller;

import com.leonovalexprog.gatewayentry.dto.UserDto;
import com.leonovalexprog.gatewayentry.dto.UserRegistrationDto;
import com.leonovalexprog.gatewayentry.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/registration")
    public UserDto addUser(@RequestBody @Valid UserRegistrationDto registrationDto) {
        return userServiceImpl.saveUser(registrationDto);
    }
}
