package com.leonovalexprog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Annotation is mandatory")
    private String annotation;
    @NotNull(message = "Category is mandatory")
    private Long category;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull(message = "Location is mandatory")
    private LocationDto location;
    @Nullable
    private Boolean paid;
    @Nullable
    private Long participantLimit;
    @Nullable
    private Boolean requestModeration;
    @NotBlank(message = "Title is mandatory")
    private String title;
}
