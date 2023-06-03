package ru.practicum.repositories.events;

import lombok.Builder;
import lombok.Data;
import ru.practicum.states.EventState;

import java.time.LocalDateTime;

@Builder
@Data
public class CombineEventFilters {
    private String text;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean paid;
    private Boolean onlyAvailable;
    private Long[] users;
    private Long[] categories;
    private EventState[] states;
}