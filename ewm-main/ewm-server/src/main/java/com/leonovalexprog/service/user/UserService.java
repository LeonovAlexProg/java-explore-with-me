package com.leonovalexprog.service.user;

import com.leonovalexprog.dto.NewUserRequest;
import com.leonovalexprog.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto newUser(NewUserRequest userShortDto);

    void deleteUser(long userId);

    List<UserDto> getUsers(List<Long> ids, int from, int size);
}
