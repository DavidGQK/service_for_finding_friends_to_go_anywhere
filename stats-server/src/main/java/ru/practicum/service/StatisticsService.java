package ru.practicum.service;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.util.List;

public interface StatisticsService {
    void add(EndpointHit endpointHit);

    List<ViewStats> getStatistic(String start, String end, String[] uris, boolean unique);
}