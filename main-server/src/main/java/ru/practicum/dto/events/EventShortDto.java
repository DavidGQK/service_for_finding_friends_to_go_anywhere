package ru.practicum.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.UserShortDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    private String eventDate;
    private Boolean paid;
    private CategoryDto category;
    private UserShortDto initiator;
    private Long confirmedRequests;
    private Long views;
}