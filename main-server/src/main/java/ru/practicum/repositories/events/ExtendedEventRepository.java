package ru.practicum.repositories.events;

import org.springframework.data.domain.Pageable;
import ru.practicum.models.Event;
import ru.practicum.states.EventSortBy;

import java.util.List;

public interface ExtendedEventRepository {
    List<Event> extendedSearchByFilters(CombineEventFilters filter, EventSortBy sort, Pageable pageable);
}