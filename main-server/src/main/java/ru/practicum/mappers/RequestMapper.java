package ru.practicum.mappers;

import ru.practicum.dto.RequestDto;
import ru.practicum.models.Request;

public class RequestMapper {
    public static RequestDto toParticipationRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getUserId())
                .event(request.getEventId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }
}