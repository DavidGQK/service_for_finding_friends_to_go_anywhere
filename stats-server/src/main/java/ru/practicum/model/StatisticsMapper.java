package ru.practicum.model;

import java.time.LocalDateTime;

import static ru.practicum.utilities.DateTime.stringToDateTime;

public class StatisticsMapper {
    public static Statistics toStatistics(EndpointHit endpointHit) {
        LocalDateTime hitTime = endpointHit.getTimestamp() == null
                ? LocalDateTime.now()
                : stringToDateTime(endpointHit.getTimestamp());

        return new Statistics(null,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                hitTime);
    }
}