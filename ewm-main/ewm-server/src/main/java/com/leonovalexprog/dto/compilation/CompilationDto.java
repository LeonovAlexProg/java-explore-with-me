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
    Long id;

    List<EventShortDto> events;

    Boolean pinned;

    String title;
}
