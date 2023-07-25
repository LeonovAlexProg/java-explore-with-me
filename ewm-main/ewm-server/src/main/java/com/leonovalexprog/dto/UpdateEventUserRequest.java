package com.leonovalexprog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    @Nullable
    @Size(min = 20, max = 2000, message = "Annotation's size is out of bounds")
    private String annotation;

    @Nullable
    private CategoryDto category;

    @Nullable
    @Size(min = 20, max = 7000, message = "Description's size is out of bounds")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "Event can not be in past")
    private LocalDateTime eventDate;

    @Nullable
    private LocationDto location;

    @Nullable
    private Boolean paid;

    @Nullable
    private Long participantLimit;

    @Nullable
    private Boolean requestModeration;

    @Nullable
    private StateAction stateAction;

    @Nullable
    @Size(min = 3, max = 120, message = "Title's size is out of bounds")
    private String title;

    public enum StateAction {
        SEND_TO_REVIEW,
        CANSEL_REVIEW
    }
}
