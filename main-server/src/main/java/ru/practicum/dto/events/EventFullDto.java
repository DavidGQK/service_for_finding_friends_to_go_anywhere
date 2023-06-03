package ru.practicum.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.states.EventState;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@SuperBuilder
public class EventFullDto extends EventShortDto {
    private String description;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private Location location;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventState state;
}