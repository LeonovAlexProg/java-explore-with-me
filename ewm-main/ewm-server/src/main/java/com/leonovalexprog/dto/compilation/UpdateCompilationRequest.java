package com.leonovalexprog.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    @Nullable
    private List<Long> events;

    @Nullable
    private Boolean pinned;

    @Nullable
    @Size(min = 1, max = 50, message = "Title size is out of bounds")
    private String title;
}
