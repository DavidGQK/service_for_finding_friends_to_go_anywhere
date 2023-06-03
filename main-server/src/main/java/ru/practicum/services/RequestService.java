package ru.practicum.services;

import ru.practicum.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto create(long userId, Long eventId);

    List<RequestDto> find(long userId);

    List<RequestDto> findRequests(long userId, long eventId);

    RequestDto remove(long userId, long requestId);

    RequestDto confirmRequest(long userId, long eventId, long reqId);

    RequestDto declineRequest(long userId, long eventId, long reqId);
}