package com.leonovalexprog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private List<Long> events = Collections.emptyList();

    private Boolean pinned = false;

    @NotNull(message = "Title is mandatory")
    @Size(min = 1, max = 50, message = "Title size is out of bounds")
    private String title;
}
