package com.leonovalexprog.service;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;
import com.leonovalexprog.mapper.RequestMapper;
import com.leonovalexprog.model.Request;
import com.leonovalexprog.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StatServiceIntegrationTest {
    private final StatService service;

    private final RequestRepository repository;

    RequestRegisterDto requestDto;

    LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        localDateTime = LocalDateTime.now();

        requestDto = RequestRegisterDto.builder()
                .app("test-app")
                .uri("/test")
                .ip("255.255.255.255")
                .timestamp(localDateTime)
                .build();
    }

    @Test
    void registerRequest() {
        service.registerRequest(requestDto);
    }

    @Test
    void getRequestsStat() {
        List<RequestResponseDto> expectedList;
        List<RequestResponseDto> actualList;

        Request request = Request.builder()
                .application(requestDto.getApp())
                .uri(requestDto.getUri())
                .ip(requestDto.getIp())
                .timestamp(localDateTime)
                .build();

        List<Request> responses = new ArrayList<>();
        responses.add(repository.save(request));

        expectedList = RequestMapper.toResponse(responses, false);
        actualList = service.getRequestsStat(localDateTime.minusDays(100), localDateTime.plusDays(100), null, false);

        Assertions.assertEquals(expectedList, actualList);
    }
}