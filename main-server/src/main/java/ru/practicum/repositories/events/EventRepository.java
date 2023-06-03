package ru.practicum.repositories.events;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Event;
import ru.practicum.states.EventSortBy;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, ExtendedEventRepository {
    @Query(nativeQuery = true, value = "select min(created) from events where id in :eventIds")
    LocalDateTime getMinCreatedDate(Long[] eventIds);

    List<Event> findEventsByOwnerId(long ownerId, Pageable pageable);

    List<Event> extendedSearchByFilters(CombineEventFilters eventFilters, EventSortBy sort, Pageable pageable);
}