package com.leonovalexprog.gatewayentry.service;

import com.leonovalexprog.gatewayentry.dto.UserDto;
import com.leonovalexprog.gatewayentry.dto.UserRegistrationDto;

public interface UserService {
    UserDto saveUser(UserRegistrationDto registrationDto);
}
