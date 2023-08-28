package com.leonovalexprog.controller;

import com.leonovalexprog.dto.UserDto;
import com.leonovalexprog.dto.UserRegistrationDto;
import com.leonovalexprog.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
