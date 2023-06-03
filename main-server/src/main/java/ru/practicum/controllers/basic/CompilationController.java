package ru.practicum.controllers.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.services.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/compilations")
public class CompilationController {
    private final CompilationService compilationService;

    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> get(@RequestParam(required = false) Boolean pinned,
                                    @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Obtaining selections of events by specified criteria");
        return compilationService.get(pinned, from, size);
    }

    @GetMapping(value = "/{compId}")
    public CompilationDto findById(@PathVariable @Positive long compId) {
        log.info("Getting a collection of events with id={}", compId);
        return compilationService.findById(compId);
    }
}