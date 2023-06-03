package ru.practicum.mappers;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CreateCompilationDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.models.Compilation;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(CreateCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> events = compilation.getCompilationEvents().stream()
                .map(o -> EventMapper.toEventShortDto(o.getEvent()))
                .collect(Collectors.toList());

        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(events)
                .build();
    }
}