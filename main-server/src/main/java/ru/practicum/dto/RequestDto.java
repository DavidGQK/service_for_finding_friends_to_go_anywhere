package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.states.RequestState;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class RequestDto {
    private Long id;
    private Long requester;
    private Long event;
    private LocalDateTime created;
    private RequestState status;
}