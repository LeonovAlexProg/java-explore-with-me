package com.leonovalexprog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
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
    @Size(max = 15, message = "Ip is too long")
    private String ip;
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(message = "Timestamp can't be in future")
    private LocalDateTime datetime;
}
