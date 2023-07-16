package com.leonovalexprog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;
import com.leonovalexprog.service.StatService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatController.class)
class StatControllerTest {
    @MockBean
    StatService statService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    RequestRegisterDto registerDto;

    RequestResponseDto responseDto;

    @Test
    void registerRequest() throws Exception {
        registerDto = RequestRegisterDto.builder()
                .app("test-app")
                .uri("/test")
                .ip("255.255.255.255")
                .build();

        mvc.perform(post("/hit")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerDto)))
                        .andExpect(status().isCreated());

        Mockito.verify(statService, Mockito.times(1))
                .registerRequest(registerDto);
    }

    @Test
    void getStatistic() throws Exception {
        registerDto = RequestRegisterDto.builder()
                .app("test-app")
                .uri("/test")
                .ip("255.255.255.255")
                .build();
        responseDto = RequestResponseDto.builder()
                .app("test-app")
                .uri("/test")
                .hits(1)
                .build();

        Mockito
                .when(statService.getRequestsStat(any(LocalDateTime.class), any(LocalDateTime.class), any(), any()))
                .thenReturn(List.of(responseDto));

        mvc.perform(get("/stats")
                .param("start", "2000-01-01 12:12:12")
                .param("end", "2050-01-01 12:12:12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].app").value(responseDto.getApp()))
                .andExpect(jsonPath("$[0].uri").value(responseDto.getUri()))
                .andExpect(jsonPath("$[0].hits").value(responseDto.getHits()));
    }
}