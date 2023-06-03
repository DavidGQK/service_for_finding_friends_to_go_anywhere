package ru.practicum.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Location {
    private float lat;
    private float lon;
}