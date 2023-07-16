package com.leonovalexprog.service;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;
import com.leonovalexprog.model.Request;
import com.leonovalexprog.repository.RequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class StatServiceUnitTest {
    @Mock
    RequestRepository repository;

    @InjectMocks
    StatServiceImpl service;

    RequestRegisterDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = RequestRegisterDto.builder()
                .app("test-app")
                .uri("/test")
                .ip("255.255.255.255")
                .datetime(LocalDateTime.now())
                .build();
    }

    @Test
    void registerRequest() {
        Request request = Request.builder()
                .application(requestDto.getApp())
                .uri(requestDto.getUri())
                .ip(requestDto.getIp())
                .datetime(requestDto.getDatetime())
                .build();

        Mockito
                .when(repository.save(request))
                .thenReturn(request);

        service.registerRequest(requestDto);

        Mockito
                .verify(repository, Mockito.times(1))
                .save(request);
    }

    @Test
    void getRequestsStat() {
        List<RequestResponseDto> expectedList;
        List<RequestResponseDto> actualList;

        LocalDateTime datetime = LocalDateTime.now();
        Request request = Request.builder()
                .id(1L)
                .application(requestDto.getApp())
                .uri(requestDto.getUri())
                .ip(requestDto.getIp())
                .datetime(datetime)
                .build();
        RequestResponseDto responseDto = RequestResponseDto.builder()
                .app(requestDto.getApp())
                .uri(requestDto.getUri())
                .hits(1)
                .build();

        Mockito
                .when(repository.findByDatetime(datetime.minusDays(100), datetime.plusDays(100)))
                .thenReturn(List.of(request));

        expectedList = List.of(responseDto);
        actualList = service.getRequestsStat(datetime.minusDays(100), datetime.plusDays(100), null, false);

        Assertions.assertEquals(expectedList, actualList);
    }
}