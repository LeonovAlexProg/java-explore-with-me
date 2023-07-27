package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.compilation.CompilationDto;
import com.leonovalexprog.model.Compilation;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation, Map<Long, Long> views) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(EventMapper.toShortDto(compilation.getEvents(), views))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static List<CompilationDto> toDto(List<Compilation> compilationList, Map<Long, Map<Long, Long>> views) {
        return compilationList.stream()
                .map(c -> CompilationMapper.toDto(c, views.get(c.getId())))
                .collect(Collectors.toList());
    }
}
