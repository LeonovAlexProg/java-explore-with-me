package com.leonovalexprog.controller;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/hit")
public class HitController {
    private final StatService statService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRequest(@RequestBody RequestRegisterDto requestRegisterDto) {
        statService.registerRequest(requestRegisterDto);
    }
}
