package ru.practicum.services;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CreateCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(CreateCompilationDto createCompilationDto);

    CompilationDto findById(long compilationId);

    void deleteById(long compilationId);

    void createEvent(long compilationId, long eventId);

    void pin(long compilationId);

    void unpin(long compilationId);

    void deleteEvent(long compilationId, long eventId);

    List<CompilationDto> get(Boolean pinned, Integer from, Integer size);
}