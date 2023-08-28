package com.leonovalexprog.service;

import com.leonovalexprog.dto.UserDto;
import com.leonovalexprog.dto.UserRegistrationDto;

public interface UserService {
    UserDto saveUser(UserRegistrationDto registrationDto);
}
