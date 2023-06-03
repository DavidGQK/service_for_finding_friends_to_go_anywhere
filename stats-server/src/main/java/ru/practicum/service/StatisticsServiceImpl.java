package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.Statistics;
import ru.practicum.model.StatisticsMapper;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.Info;
import ru.practicum.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ru.practicum.utilities.DateTime.stringToDateTime;

@RequiredArgsConstructor
@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticRepository statisticRepository;

    @Override
    public void add(EndpointHit endpointHit) {
        Statistics statistic = StatisticsMapper.toStatistics(endpointHit);
        statisticRepository.save(statistic);
    }

    @Override
    public List<ViewStats> getStatistic(String start, String end, String[] uris, boolean unique) {
        LocalDateTime startDate = stringToDateTime(start);
        LocalDateTime endDate = stringToDateTime(end);
        List<ViewStats> result = new ArrayList<>();

        Arrays.stream(uris).forEach(uri -> {
            Optional<ViewStats> stat = getViewStatistic(startDate, endDate, uri, unique);
            stat.ifPresent(result::add);
        });
        return result;
    }

    private Optional<ViewStats> getViewStatistic(LocalDateTime start, LocalDateTime end,
                                                 String uri, boolean unique) {
        Info info = statisticRepository.getStatistics(start, end, uri);

        if (info == null) {
            return Optional.empty();
        }
        long count = unique ? info.getCountUniqueIp() : info.getCountAll();
        return Optional.of(new ViewStats(info.getAppName(), uri, count));
    }
}