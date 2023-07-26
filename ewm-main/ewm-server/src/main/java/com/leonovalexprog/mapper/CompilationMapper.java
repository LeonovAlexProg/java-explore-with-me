package com.leonovalexprog.mapper;

import com.leonovalexprog.dto.compilation.CompilationDto;
import com.leonovalexprog.model.Compilation;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(EventMapper.toShortDto(compilation.getEvents()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static List<CompilationDto> toDto(List<Compilation> compilationList) {
        return compilationList.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }
}
