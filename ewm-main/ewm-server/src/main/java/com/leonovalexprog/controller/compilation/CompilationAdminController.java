package com.leonovalexprog.controller.compilation;

import com.leonovalexprog.dto.compilation.CompilationDto;
import com.leonovalexprog.dto.compilation.NewCompilationDto;
import com.leonovalexprog.dto.compilation.UpdateCompilationRequest;
import com.leonovalexprog.service.compilation.CompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto postCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Post new compilation (events = {}, pinned = {}, title = {})", newCompilationDto.getEvents(), newCompilationDto.getPinned(), newCompilationDto.getTitle());
        return compilationService.postCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Delete compilation (comp id = {})", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@PathVariable Long compId,
                                           @Validated @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Patch compilation (comp id = {}, events = {}, pinned = {}, title = {})",
                compId, updateCompilationRequest.getEvents(), updateCompilationRequest.getPinned(), updateCompilationRequest.getTitle());

        return compilationService.patchCompilation(compId, updateCompilationRequest);
    }
}
