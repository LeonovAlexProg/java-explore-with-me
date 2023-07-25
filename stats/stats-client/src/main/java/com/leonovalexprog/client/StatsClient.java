package com.leonovalexprog.client;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class StatsClient {
    private final String statsServerUrl;
    private final RestTemplate restTemplate;

    public StatsClient(@Value("${stats.server.url}") String statsServerUrl) {
        this.statsServerUrl = statsServerUrl;
        restTemplate = new RestTemplate();
    }

    public void registerRequest(String app, HttpServletRequest request) {
        RequestRegisterDto registerDto = RequestRegisterDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .datetime(LocalDateTime.now())
                .build();
        log.info("Send request data to stats-service (app = {}, uri = {}, ip = {}, timestamp = {})",
                registerDto.getApp(),
                registerDto.getUri(),
                registerDto.getIp(),
                registerDto.getDatetime());

        restTemplate.postForObject(statsServerUrl + "/hit", registerDto, Object.class);
    }

    public List<RequestResponseDto> getRequestsStat(LocalDateTime start,
                                                    LocalDateTime end,
                                                    List<String> uris,
                                                    Boolean unique) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(statsServerUrl + "/stats");
        builder.queryParam("start", dateTimeConverter(start));
        builder.queryParam("end", dateTimeConverter(end));
        if (uris != null)
            builder.queryParam("uris", String.join(",", uris));
        if (unique != null)
            builder.queryParam("unique", unique);
        URI uri = builder.build(false).toUri();

        RequestResponseDto[] stats = restTemplate.getForObject(uri, RequestResponseDto[].class);

        if (stats != null) {
            return new ArrayList<>(Arrays.asList(stats));
        } else {
            return Collections.emptyList();
        }
    }

    private String dateTimeConverter(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
