package com.leonovalexprog.controller;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;
import com.leonovalexprog.service.StatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatController {
    private final StatService statService;

    @PostMapping(value = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRequest(@Valid @RequestBody RequestRegisterDto requestRegisterDto) {
        log.info("Register request (app = {}, uri = {}, ip = {}, timestamp = {})",
                requestRegisterDto.getApp(),
                requestRegisterDto.getUri(),
                requestRegisterDto.getIp(),
                requestRegisterDto.getDatetime());
        statService.registerRequest(requestRegisterDto);
    }

    @GetMapping(value = "/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponseDto> getStatistic(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                 @RequestParam(required = false) List<String> uris,
                                                 @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Get statistic (from = {}, to = {}, uris = {}, unique = {})",
                start,
                end,
                uris,
                unique);
        return statService.getRequestsStat(start, end, uris, unique);
    }
}
