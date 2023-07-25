package com.leonovalexprog.service.compilation;

import com.leonovalexprog.dto.CompilationDto;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.mapper.CompilationMapper;
import com.leonovalexprog.model.Compilation;
import com.leonovalexprog.model.Event;
import com.leonovalexprog.repository.CompilationRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<Boolean> pinnedFilter;

        if (pinned != null)
            pinnedFilter = List.of(pinned);
        else
            pinnedFilter = List.of(Boolean.TRUE, Boolean.FALSE);

        List<Compilation> compilations = compilationRepository.findAllWherePinned(pinnedFilter, PageRequest.of(from / size, size));

        return CompilationMapper.toDto(compilations);
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Compilation with id=%d was not found", compId)));

        return CompilationMapper.toDto(compilation);
    }
}
