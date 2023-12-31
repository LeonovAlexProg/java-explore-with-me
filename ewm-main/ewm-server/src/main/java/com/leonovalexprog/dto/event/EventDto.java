package com.leonovalexprog.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leonovalexprog.dto.location.EventLocationDto;
import com.leonovalexprog.dto.user.UserShortDto;
import com.leonovalexprog.dto.category.CategoryDto;
import com.leonovalexprog.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private EventLocationDto location;
    private Boolean paid;
    private Long participantLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private Event.State state;
    private String title;
    private Long views;
}
