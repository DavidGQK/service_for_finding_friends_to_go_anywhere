package ru.practicum.mappers;

import ru.practicum.dto.events.*;
import ru.practicum.models.Category;
import ru.practicum.models.Event;
import ru.practicum.states.EventState;
import ru.practicum.utilities.DateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class EventMapper {
    public static Event toEvent(CreateEventDto createEventDto) {
        boolean paid = false;
        Integer participantLimit = 0;
        boolean requestModeration = false;

        if (createEventDto.getPaid() != null) {
            paid = createEventDto.getPaid();
        }

        if (createEventDto.getRequestModeration() != null) {
            requestModeration = createEventDto.getRequestModeration();
        }

        if (createEventDto.getParticipantLimit() != null) {
            participantLimit = createEventDto.getParticipantLimit();
        }

        return Event.builder()
                .title(createEventDto.getTitle())
                .annotation(createEventDto.getAnnotation())
                .description(createEventDto.getDescription())
                .eventDate(extractDateTimeFromString(createEventDto.getEventDate()))
                .created(LocalDateTime.now())
                .lat(createEventDto.getLocation().getLat())
                .lon(createEventDto.getLocation().getLon())
                .paid(paid)
                .participantLimit(participantLimit)
                .requestModeration(requestModeration)
                .category(new Category(createEventDto.getCategory(), ""))
                .state(EventState.PENDING)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(DateTime.dateTimeToString(event.getEventDate()))
                .paid(event.getPaid())
                .initiator(UserMapper.toUserShortDto(event.getOwner()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(0L)
                .views(0L)
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(DateTime.dateTimeToString(event.getEventDate()))
                .createdOn(event.getCreated())
                .publishedOn(event.getPublished())
                .location(new Location(event.getLat(), event.getLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .initiator(UserMapper.toUserShortDto(event.getOwner()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .state(event.getState())
                .confirmedRequests(0L)
                .views(0L)
                .build();
    }

    public static void prepareToUpdate(UpdateEventRequest updateEventRequest, Event event) {
        prepareUpdateEvent((UpdateEvent) updateEventRequest, event);
    }

    public static void prepareToUpdate(AdminUpdateEvent adminUpdateEvent, Event event) {
        prepareUpdateEvent((UpdateEvent) adminUpdateEvent, event);
        if (adminUpdateEvent.getLocation() != null) {
            event.setLat(adminUpdateEvent.getLocation().getLat());
            event.setLon(adminUpdateEvent.getLocation().getLon());
        }
    }

    private static LocalDateTime extractDateTimeFromString(String value) {
        try {
            return DateTime.stringToDateTime(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(String.format("Error in time format '%s': %s",
                    "eventDate", e.getMessage()));
        }
    }

    private static void prepareUpdateEvent(UpdateEvent updateEvent, Event event) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(extractDateTimeFromString(updateEvent.getEventDate()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(new Category(updateEvent.getCategory(), ""));
        }
    }
}