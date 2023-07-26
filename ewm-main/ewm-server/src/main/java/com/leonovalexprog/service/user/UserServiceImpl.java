package com.leonovalexprog.service.user;

import com.leonovalexprog.dto.request.NewUserRequest;
import com.leonovalexprog.dto.user.UserDto;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.FieldValueExistsException;
import com.leonovalexprog.mapper.UserMapper;
import com.leonovalexprog.model.User;
import com.leonovalexprog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto newUser(NewUserRequest userShortDto) {
        User user = User.builder()
                .name(userShortDto.getName())
                .email(userShortDto.getEmail())
                .build();

        try {
            User newUser = userRepository.saveAndFlush(user);
            return UserMapper.toDto(newUser);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }

    @Override
    public void deleteUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotExistsException(String.format("User with id=%d was not found", userId));
        }

        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        if (from < 0 || size < 0) {
            throw new IllegalArgumentException("From and Size must be positive or zero");
        }

        List<User> users;

        if (ids != null) {
            users = userRepository.findAllById(ids);
        } else {
            users = userRepository.findAll(PageRequest.of(from / size, size)).getContent();
        }

        return UserMapper.toDto(users);
    }
}
