package com.leonovalexprog.gatewayentry.mapper;

import com.leonovalexprog.gatewayentry.dto.UserDto;
import com.leonovalexprog.gatewayentry.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
