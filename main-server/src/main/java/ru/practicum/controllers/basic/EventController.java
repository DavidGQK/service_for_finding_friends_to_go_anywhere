package ru.practicum.controllers.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.repositories.events.CombineEventFilters;
import ru.practicum.services.EventService;
import ru.practicum.states.EventSortBy;
import ru.practicum.statistics.service.StatisticService;
import ru.practicum.utilities.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
public class EventController {
    private final StatisticService statisticService;
    private final EventService eventService;

    public EventController(EventService eventService, StatisticService statisticService) {
        this.eventService = eventService;
        this.statisticService = statisticService;
    }

    @GetMapping(value = "/{eventId}")
    EventFullDto findEventById(@PathVariable @Positive long eventId,
                               HttpServletRequest request) {
        statisticService.addStatistics(request);
        log.info("Search for an event with id={}", eventId);
        return eventService.findEventById(eventId);
    }

    @GetMapping
    public List<EventShortDto> findShortEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) Long[] categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(required = false) Boolean onlyAvailable,
                                               @RequestParam(required = false) EventSortBy sort,
                                               @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                               HttpServletRequest request) {

        CombineEventFilters combineEventFilters = CombineEventFilters.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(DateTime.stringToDateTime(rangeStart))
                .rangeEnd(DateTime.stringToDateTime(rangeEnd))
                .onlyAvailable(onlyAvailable)
                .build();
        log.info("Obtaining a list of events according to the specified parameters");
        return eventService.findShortEvents(combineEventFilters, sort, from, size);
    }
}