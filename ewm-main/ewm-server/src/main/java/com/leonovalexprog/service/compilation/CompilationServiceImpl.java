package com.leonovalexprog.service.compilation;

import com.leonovalexprog.dto.compilation.CompilationDto;
import com.leonovalexprog.dto.compilation.NewCompilationDto;
import com.leonovalexprog.dto.compilation.UpdateCompilationRequest;
import com.leonovalexprog.exception.exceptions.EntityNotExistsException;
import com.leonovalexprog.exception.exceptions.FieldValueExistsException;
import com.leonovalexprog.mapper.CompilationMapper;
import com.leonovalexprog.model.Compilation;
import com.leonovalexprog.model.Event;
import com.leonovalexprog.repository.CompilationRepository;
import com.leonovalexprog.repository.EventsRepository;
import com.leonovalexprog.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventsRepository eventsRepository;

    private final EventService eventService;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<Boolean> pinnedFilter;

        if (pinned != null)
            pinnedFilter = List.of(pinned);
        else
            pinnedFilter = List.of(Boolean.TRUE, Boolean.FALSE);

        List<Compilation> compilations = compilationRepository.findAllWherePinned(pinnedFilter, PageRequest.of(from / size, size));

        return CompilationMapper.toDto(compilations, mapEventsViewsByCompilations(compilations));
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Compilation with id=%d was not found", compId)));

        return CompilationMapper.toDto(compilation, getEventsViewsByCompilation(compilation));
    }

    @Override
    @Transactional
    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = Collections.emptyList();

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty())
            events = eventsRepository.findAllById(newCompilationDto.getEvents());

        Compilation compilation = Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();

        try {
            Compilation newCompilation = compilationRepository.saveAndFlush(compilation);
            return CompilationMapper.toDto(newCompilation, getEventsViewsByCompilation(newCompilation));
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Compilation with id=%d was not found", compId)));

        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationDto patchCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotExistsException(String.format("Compilation with id=%d was not found", compId)));

        List<Event> events = null;
        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty())
            events = eventsRepository.findAllById(updateCompilationRequest.getEvents());

        if (events != null)
            compilation.setEvents(events);
        if (updateCompilationRequest.getPinned() != null)
            compilation.setPinned(updateCompilationRequest.getPinned());
        if (updateCompilationRequest.getTitle() != null)
            compilation.setTitle(updateCompilationRequest.getTitle());

        try {
            Compilation newCompilation = compilationRepository.saveAndFlush(compilation);
            return CompilationMapper.toDto(newCompilation, getEventsViewsByCompilation(newCompilation));
        } catch (DataIntegrityViolationException exception) {
            throw new FieldValueExistsException(exception.getMessage());
        }
    }

    public Map<Long, Long> getEventsViewsByCompilation(Compilation compilation) {
        return eventService.getEventsViews(compilation.getEvents());
    }

    public Map<Long, Map<Long, Long>> mapEventsViewsByCompilations(List<Compilation> compilations) {
        return compilations.stream()
                .collect(Collectors.toMap(Compilation::getId, this::getEventsViewsByCompilation));
    }
}
