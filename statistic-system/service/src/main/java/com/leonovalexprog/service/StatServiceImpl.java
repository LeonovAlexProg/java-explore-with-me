package com.leonovalexprog.service;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.model.Request;
import com.leonovalexprog.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
