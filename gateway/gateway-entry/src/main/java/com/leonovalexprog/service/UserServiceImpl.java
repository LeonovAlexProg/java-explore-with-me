package com.leonovalexprog.service;

import com.leonovalexprog.dto.UserDto;
import com.leonovalexprog.dto.UserRegistrationDto;
import com.leonovalexprog.exception.exceptions.IncorrectPasswordException;
import com.leonovalexprog.exception.exceptions.UserExistsException;
import com.leonovalexprog.mapper.UserMapper;
import com.leonovalexprog.model.User;
import com.leonovalexprog.repository.RoleRepository;
import com.leonovalexprog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto saveUser(UserRegistrationDto registrationDto) {
        if (!validatePassword(registrationDto)) {
            throw new IncorrectPasswordException("Пароли не совпадают");
        }

        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new UserExistsException("Имя пользователя занято");
        }

        User user = User.builder()
                .username(registrationDto.getUsername())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .roles(Collections.singleton(roleRepository.findByName("ROLE_PRIVATE")))
                .build();

        try {
            User newUser = userRepository.saveAndFlush(user);
            return UserMapper.toDto(newUser);
        } catch (DataIntegrityViolationException exception) {
            throw new UserExistsException(exception.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    private boolean validatePassword(UserRegistrationDto registrationDto) {
        return registrationDto.getPassword().equals(registrationDto.getPasswordConfirm());
    }
}