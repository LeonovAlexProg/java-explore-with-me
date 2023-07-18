package com.leonovalexprog.controller.user;

import com.leonovalexprog.dto.NewUserRequest;
import com.leonovalexprog.dto.UserDto;
import com.leonovalexprog.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@Slf4j
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@Valid @RequestBody NewUserRequest userShortDto) {
        log.info("Post new user (email = {}, name = {})", userShortDto.getEmail(), userShortDto.getName());
        return userService.newUser(userShortDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Delete user (id = {})", userId);
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Get users (ids = {}, from = {}, size = {})", ids, from, size);
        return userService.getUsers(ids, from, size);
    }
}
