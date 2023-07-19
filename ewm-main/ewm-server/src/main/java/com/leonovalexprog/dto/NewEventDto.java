package com.leonovalexprog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Annotation is mandatory")
    @Size(max = 2000, message = "Annotation to long")
    @Size(min = 20, message = "Annotation to short")
    private String annotation;
    @NotNull(message = "Category is mandatory")
    private Long category;
    @NotBlank(message = "Description is mandatory")
    @Size(max = 7000, message = "Description to long")
    @Size(min = 20, message = "Description to short")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Event date is mandatory")
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
    @Size(max = 120, message = "Title to long")
    @Size(min = 3, message = "Title to short")
    private String title;
}
