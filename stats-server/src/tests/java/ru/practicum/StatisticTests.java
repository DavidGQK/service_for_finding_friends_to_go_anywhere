package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.service.StatisticsService;

import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatisticTests {
    private final StatisticsService statisticsService;

    @Test
    public void add() {
        EndpointHit hit = EndpointHit.builder()
                .app("app")
                .uri("/test/1")
                .ip("192.168.1.1")
                .build();

        statisticsService.add(hit);
        String[] urisList = {"/test/1"};
        List<ViewStats> viewStatistics = statisticsService.getStatistic(
                "2021-01-01 01:01:01",
                "2023-12-12 12:12:12",
                urisList, false);

        Assertions.assertNotNull(viewStatistics);
        Assertions.assertEquals(1, viewStatistics.size());
    }
}