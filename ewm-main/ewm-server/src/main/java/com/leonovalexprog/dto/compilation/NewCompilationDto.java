package com.leonovalexprog.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @Nullable
    private List<Long> events = Collections.emptyList();

    @Nullable
    private Boolean pinned = false;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 50, message = "Title size is out of bounds")
    private String title;
}
