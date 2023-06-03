package ru.practicum.services;

import ru.practicum.dto.SubscriptionDto;
import ru.practicum.dto.events.EventShortDto;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDto create(long userId, long friendId);

    void delete(long id);

    List<EventShortDto> getEvents(long userId);

    List<SubscriptionDto> getSubscriptions(long userId);
}