package com.leonovalexprog.service;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;
import com.leonovalexprog.mapper.RequestMapper;
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
                .datetime(requestRegisterDto.getDatetime())
                .build();

        repository.save(newRequest);
    }

    @Override
    public List<RequestResponseDto> getRequestsStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<Request> requests;
        List<RequestResponseDto> responses;

        if (uris == null || uris.isEmpty()) {
            requests = repository.findByDatetime(start, end);
        } else {
            requests = repository.findByDatetimeAndUris(start, end, uris);
        }

        responses = RequestMapper.toResponse(requests, unique);

        return responses;
    }
}
