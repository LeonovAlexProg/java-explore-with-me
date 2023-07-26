package com.leonovalexprog.service.user;

import com.leonovalexprog.dto.request.NewUserRequest;
import com.leonovalexprog.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto newUser(NewUserRequest userShortDto);

    void deleteUser(long userId);

    List<UserDto> getUsers(List<Long> ids, int from, int size);
}
