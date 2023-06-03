package ru.practicum.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CreateCompilationDto;
import ru.practicum.services.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/compilations")
public class CompilationControllerAdmin {
    private final CompilationService compilationService;

    public CompilationControllerAdmin(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto create(@Valid @RequestBody CreateCompilationDto createCompilationDto) {
        log.info("A selection {} was created!", createCompilationDto);
        return compilationService.create(createCompilationDto);
    }

    @PatchMapping(value = "/{compId}/events/{eventId}")
    public void createEvent(@PathVariable @Positive long compId,
                            @PathVariable @Positive long eventId) {
        log.info("Event {} in the selection {} is created!", eventId, compId);
        compilationService.createEvent(compId, eventId);
    }

    @DeleteMapping(value = "/{compId}")
    public void deleteById(@PathVariable @Positive long compId) {
        compilationService.deleteById(compId);
        log.info("Selection with id={} deleted!", compId);
    }

    @DeleteMapping(value = "/{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable @Positive long compId,
                            @PathVariable @Positive long eventId) {
        compilationService.deleteEvent(compId, eventId);
        log.info("Event {} in the selection {} deleted!", eventId, compId);
    }

    @PatchMapping(value = "/{compId}/pin")
    public void pin(@PathVariable @Positive long compId) {
        compilationService.pin(compId);
        log.info("A selection with id={} is fixed!", compId);
    }

    @DeleteMapping(value = "/{compId}/pin")
    public void unpin(@PathVariable @Positive long compId) {
        compilationService.unpin(compId);
        log.info("The set with id={} is detached!", compId);
    }
}