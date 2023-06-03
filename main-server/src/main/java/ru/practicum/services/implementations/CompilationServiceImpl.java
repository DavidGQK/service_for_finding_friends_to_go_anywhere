package ru.practicum.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CreateCompilationDto;
import ru.practicum.errors.exceptions.NotFoundException;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.models.Compilation;
import ru.practicum.models.Event;
import ru.practicum.models.EventsCompilation;
import ru.practicum.repositories.CompilationRepository;
import ru.practicum.repositories.EventsCompilationRepository;
import ru.practicum.repositories.events.EventRepository;
import ru.practicum.services.CompilationService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final EventsCompilationRepository eventsCompilationRepository;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public CompilationDto create(CreateCompilationDto dto) {
        Compilation compilation = CompilationMapper.toCompilation(dto);
        final Compilation compilationToSave = compilationRepository.save(compilation);

        List<EventsCompilation> compilationList = dto.getEvents().stream()
                .map(eventId -> new EventsCompilation(null, compilationToSave.getId(),
                        checkEvent(eventId)))
                .collect(Collectors.toList());

        compilationList.forEach(eventsCompilationRepository::save);
        compilationToSave.setCompilationEvents(compilationList);
        return CompilationMapper.toCompilationDto(compilationToSave);
    }

    @Override
    public CompilationDto findById(long id) {
        Compilation compilation = checkCompilation(id);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        checkCompilation(id);
        compilationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void createEvent(long compilationId, long eventId) {
        EventsCompilation eventsList = new EventsCompilation(null, compilationId, checkEvent(eventId));
        eventsCompilationRepository.save(eventsList);
    }

    @Override
    @Transactional
    public void pin(long id) {
        Compilation compilation = checkCompilation(id);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void unpin(long id) {
        Compilation compilation = checkCompilation(id);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteEvent(long compilationId, long eventId) {
        EventsCompilation eventsList =
                eventsCompilationRepository.findByCompIdAndEventId(compilationId, eventId)
                        .orElseThrow(() -> new NotFoundException("The event with this id does not exist!"));

        eventsCompilationRepository.deleteById(eventsList.getId());
    }

    @Override
    public List<CompilationDto> get(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned == null) {
            return compilationRepository.findAll(pageable).stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findByPinned(pinned, pageable).stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
    }

    private Event checkEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The event with this id does not exist!"));
    }

    private Compilation checkCompilation(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no sample with this id!"));
    }
}