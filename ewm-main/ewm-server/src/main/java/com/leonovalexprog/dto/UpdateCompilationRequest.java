package com.leonovalexprog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    @Nullable
    List<Long> events;

    @Nullable
    Boolean pinned;

    @Nullable
    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 50, message = "Title size is out of bounds")
    String title;
}
