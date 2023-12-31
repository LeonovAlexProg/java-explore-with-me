package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.RequestResponseDto;
import com.leonovalexprog.model.Request;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public static List<RequestResponseDto> toResponse(List<Request> requests, boolean isUnique) {
        List<RequestResponseDto> responses;

        Map<String, List<Request>> requestsByUri = requests.stream()
                .collect(Collectors.groupingBy(Request::getUri));

        if (!isUnique) {
            responses = requestsByUri.keySet().stream()
                    .map(key -> new RequestResponseDto(requestsByUri.get(key).get(0).getApplication(), key, requestsByUri.get(key).size()))
                    .collect(Collectors.toList());
        } else {
            responses = requestsByUri.keySet().stream()
                    .map(key -> new RequestResponseDto(requestsByUri.get(key).get(0).getApplication(), key, (int) requestsByUri.get(key).stream()
                            .map(Request::getIp)
                            .distinct()
                            .count()))
                    .collect(Collectors.toList());
        }

        responses.sort(Comparator.comparing(RequestResponseDto::getHits).reversed());

        return responses;
    }
}
