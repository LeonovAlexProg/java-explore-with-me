package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.UserDto;
import com.leonovalexprog.model.User;
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
