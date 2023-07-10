package com.leonovalexprog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestResponseDto {
    private String app;
    private String uri;
    private int hits;
}
