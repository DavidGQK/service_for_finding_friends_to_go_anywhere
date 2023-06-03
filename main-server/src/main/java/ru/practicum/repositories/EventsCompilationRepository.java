package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.models.EventsCompilation;

import java.util.Optional;

public interface EventsCompilationRepository extends JpaRepository<EventsCompilation, Long> {
    Optional<EventsCompilation> findByCompIdAndEventId(Long compId, Long eventId);
}