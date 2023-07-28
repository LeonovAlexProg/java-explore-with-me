package com.leonovalexprog.service.compilation;

import com.leonovalexprog.dto.compilation.CompilationDto;
import com.leonovalexprog.dto.compilation.NewCompilationDto;
import com.leonovalexprog.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(Long compId);

    CompilationDto postCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto patchCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
