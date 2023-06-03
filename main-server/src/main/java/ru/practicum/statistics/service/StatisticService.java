package ru.practicum.statistics.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public interface StatisticService {
    Map<Long, Long> getEventViewCount(Set<Long> eventIds);

    long getEventViewCount(long eventId);

    void addStatistics(HttpServletRequest httpRequest);
}