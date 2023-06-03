package ru.practicum.dto.events;

import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class UpdateEvent {
    private String title;
    private String annotation;
    private String description;
    private String eventDate;
    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    @Positive
    private Long category;
}