package com.leonovalexprog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegisterDto {
    @NotBlank(message = "App is mandatory")
    private String app;
    @NotBlank(message = "Uri is mandatory")
    private String uri;
    @NotBlank(message = "Ip is mandatory")
    @Size(min = 7, message = "Ip is too short")
    private String ip;
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(message = "Timestamp can't be in future")
    private LocalDateTime datetime;
}
