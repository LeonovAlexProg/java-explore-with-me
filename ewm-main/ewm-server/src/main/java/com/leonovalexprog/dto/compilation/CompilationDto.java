package com.leonovalexprog.dto.compilation;

import com.leonovalexprog.dto.event.EventShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private Long id;

    private List<EventShortDto> events;

    private Boolean pinned;

    private String title;
}
