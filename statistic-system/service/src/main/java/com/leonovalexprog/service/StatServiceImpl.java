package com.leonovalexprog.service;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;
import com.leonovalexprog.model.Request;
import com.leonovalexprog.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final RequestRepository repository;

    public void registerRequest(final RequestRegisterDto requestRegisterDto) {
        Request newRequest = Request.builder()
                .application(requestRegisterDto.getApp())
                .uri(requestRegisterDto.getUri())
                .ip(requestRegisterDto.getIp())
                .timestamp(requestRegisterDto.getTimestamp())
                .build();

        repository.save(newRequest);
    }

    @Override
    public List<RequestResponseDto> getRequestStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return null;
    }
}
