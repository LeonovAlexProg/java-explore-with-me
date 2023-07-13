package com.leonovalexprog.client;

import com.leonovalexprog.dto.RequestRegisterDto;
import com.leonovalexprog.dto.RequestResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StatsClient {
    private final String statsServerUrl;
    private final RestTemplate restTemplate;

    public StatsClient(@Value("${stats.server.url}") String statsServerUrl) {
        this.statsServerUrl = statsServerUrl;
        restTemplate = new RestTemplate();
    }

    public void registerRequest(RequestRegisterDto requestRegisterDto) {
        restTemplate.postForObject(statsServerUrl + "/hit", requestRegisterDto, Object.class);
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
