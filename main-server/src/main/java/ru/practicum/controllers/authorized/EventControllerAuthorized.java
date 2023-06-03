package ru.practicum.controllers.authorized;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.events.CreateEventDto;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.events.UpdateEventRequest;
import ru.practicum.services.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
public class EventControllerAuthorized {
    private final EventService eventService;

    public EventControllerAuthorized(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/{userId}/events")
    public EventFullDto create(@PathVariable @Positive long userId, @Valid @RequestBody CreateEventDto createEventDto) {
        log.info("The user with id={} created an event {}", userId, createEventDto);
        return eventService.create(userId, createEventDto);
    }

    @PatchMapping(value = "/{userId}/events")
    public EventFullDto update(@PathVariable @Positive long userId,
                               @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("The user with id={} updated the event {}", userId, updateEventRequest);
        return eventService.update(userId, updateEventRequest);
    }

    @GetMapping(value = "/{userId}/events")
    public List<EventShortDto> findUserEvents(@PathVariable @Positive long userId, @RequestParam(required = false,
            defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("The user with id={} will participate in the following events: ", userId);
        return eventService.findUserEvents(userId, from, size);
    }

    @GetMapping(value = "/{userId}/events/{eventId}")
    public EventFullDto findUserEventById(@PathVariable @Positive long userId, @PathVariable @Positive long eventId) {
        log.info("Getting information about an event with id={} added by a user with id={}", eventId, userId);
        return eventService.findUserEventById(userId, eventId);
    }

    @PatchMapping(value = "/{userId}/events/{eventId}")
    public EventFullDto removeUserEvent(@PathVariable @Positive long userId, @PathVariable @Positive long eventId) {
        log.info("The user with id={} canceled the event with id={}", userId, eventId);
        return eventService.removeUserEvent(userId, eventId);
    }
}