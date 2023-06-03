package ru.practicum.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.errors.exceptions.UnavailableStatisticsException;
import ru.practicum.statistics.model.ViewStats;
import ru.practicum.repositories.events.EventRepository;
import ru.practicum.statistics.client.HttpClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class StatisticServiceImpl implements StatisticService {
    private final HttpClient client;
    private final EventRepository eventRepository;

    @Override
    public Map<Long, Long> getEventViewCount(Set<Long> eventIds) {
        Long[] ids = eventIds.toArray(new Long[eventIds.size()]);
        LocalDateTime minDate = eventRepository.getMinCreatedDate(ids);
        Map<String, Long> uris = eventIds.stream().collect(Collectors.toMap((l -> "/events/" + l.toString()),
                (l -> l)));

        ResponseEntity<List<ViewStats>> answer = client.getStatistics(
                minDate,
                LocalDateTime.now(),
                uris.keySet().toArray(new String[uris.keySet().size()]),
                false);

        if (answer.getStatusCode() != HttpStatus.OK) {
            throw new UnavailableStatisticsException("Error in the statistics service! " + answer.getStatusCode());
        }
        List<ViewStats> viewStats = answer.getBody();
        Map<Long, Long> result = eventIds.stream().collect(Collectors.toMap(l -> l, l -> 0L));

        viewStats.forEach(vs -> {
            Long eventId = uris.get(vs.getUri());
            Long viewCount = vs.getHits();
            result.put(eventId, viewCount);
        });
        return result;
    }

    @Override
    public long getEventViewCount(long id) {
        return getEventViewCount(Set.of(id)).get(id);
    }

    @Override
    public void addStatistics(HttpServletRequest request) {
        client.addStatistics(request.getRequestURI(), request.getRemoteAddr());
    }
}