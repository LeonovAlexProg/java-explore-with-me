package com.leonovalexprog.controller;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;
import com.leonovalexprog.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponseDto> getStatistic(@RequestParam LocalDateTime start,
                                                 @RequestParam LocalDateTime end,
                                                 @RequestParam List<String> uris,
                                                 @RequestParam Boolean unique) {
        return statService.getRequestStat(start, end, uris, unique);
    }
}
