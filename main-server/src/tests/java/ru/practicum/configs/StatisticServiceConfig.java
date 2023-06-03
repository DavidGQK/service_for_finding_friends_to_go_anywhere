package ru.practicum.configs;

import ru.practicum.statistics.service.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StatisticServiceConfig implements StatisticService {

    @Override
    public Map<Long, Long> getEventViewCount(Set<Long> eventIds) {
        return eventIds.stream()
                .collect(Collectors.toMap(l -> l, l -> 0L));
    }

    @Override
    public long getEventViewCount(long eventId) {
        return 0;
    }

    @Override
    public void addStatistics(HttpServletRequest httpRequest) {
    }
}