package com.leonovalexprog.client;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class StatsClientTest {
    private final StatsClient statsClient = new StatsClient("http://localhost:8080");

    private RequestRegisterDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = RequestRegisterDto.builder()
                .app("test-app")
                .uri("/test")
                .ip("255.255.255.255")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    void registerRequest() {
        statsClient.registerRequest(requestDto);
    }

    @Test
    void getRequestsStat() {
        List<RequestResponseDto> expectedList = new ArrayList<>();
        List<RequestResponseDto> actualList;

        RequestResponseDto expectedResponse = RequestResponseDto.builder()
                .app("test-app")
                .uri("/test")
                .hits(1)
                .build();
        expectedList.add(expectedResponse);

        statsClient.registerRequest(requestDto);

        actualList = statsClient.getRequestsStat(LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(10),
                null,
                null);

        Assertions.assertEquals(expectedList, actualList);
    }
}