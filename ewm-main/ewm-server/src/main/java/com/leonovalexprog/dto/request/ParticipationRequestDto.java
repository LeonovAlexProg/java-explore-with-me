package com.leonovalexprog.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leonovalexprog.model.ParticipationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private ParticipationRequest.Status status;
}
