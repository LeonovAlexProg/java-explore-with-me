package com.leonovalexprog.service.compilation;

import com.leonovalexprog.dto.CompilationDto;
import com.leonovalexprog.dto.NewCompilationDto;
import com.leonovalexprog.dto.UpdateCompilationRequest;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.FieldValueExistsException;
import com.leonovalexprog.mapper.CompilationMapper;
import com.leonovalexprog.model.Compilation;
import com.leonovalexprog.model.Event;
import com.leonovalexprog.repository.CompilationRepository;
import com.leonovalexprog.repository.EventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventsRepository eventsRepository;

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

    @Override
    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = null;

        if (!newCompilationDto.getEvents().isEmpty())
            events = eventsRepository.findAllById(newCompilationDto.getEvents());

        Compilation compilation = Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();

        try {
            Compilation newCompilation = compilationRepository.saveAndFlush(compilation);
            return CompilationMapper.toDto(newCompilation);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Compilation with id=%d was not found", compId)));

        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto patchCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Compilation with id=%d was not found", compId)));

        List<Event> events = null;
        if (!updateCompilationRequest.getEvents().isEmpty())
            events = eventsRepository.findAllById(updateCompilationRequest.getEvents());

        if (events != null)
            compilation.setEvents(events);
        if (updateCompilationRequest.getPinned() != null)
            compilation.setPinned(updateCompilationRequest.getPinned());
        if (updateCompilationRequest.getTitle() != null)
            compilation.setTitle(updateCompilationRequest.getTitle());

        try {
            Compilation newCompilation = compilationRepository.saveAndFlush(compilation);
            return CompilationMapper.toDto(newCompilation);
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }
}
