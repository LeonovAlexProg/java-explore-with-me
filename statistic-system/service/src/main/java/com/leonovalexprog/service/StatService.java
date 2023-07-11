package com.leonovalexprog.service;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void registerRequest(final RequestRegisterDto requestRegisterDto);

    List<RequestResponseDto> getRequestStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
