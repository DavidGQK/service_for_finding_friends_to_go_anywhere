package ru.practicum.services;

import ru.practicum.dto.events.*;
import ru.practicum.repositories.events.CombineEventFilters;
import ru.practicum.states.EventSortBy;

import java.util.List;

public interface EventService {
    EventFullDto create(long ownerId, CreateEventDto createEventDto);

    EventFullDto update(long eventId, AdminUpdateEvent adminUpdateEvent);

    EventFullDto update(long userId, UpdateEventRequest updateEventRequest);

    EventFullDto findEventById(Long eventId);

    EventFullDto publish(long eventId);

    EventFullDto decline(long eventId);

    <T extends EventShortDto> void fullFillDto(List<T> listDto);

    List<EventShortDto> findShortEvents(CombineEventFilters filters, EventSortBy sort, Integer from, Integer size);

    List<EventFullDto> findFullEvents(CombineEventFilters filters, Integer from, Integer size);

    List<EventShortDto> findUserEvents(long userId, Integer from, Integer size);

    EventFullDto findUserEventById(long userId, long eventId);

    EventFullDto removeUserEvent(long userId, long eventId);
}