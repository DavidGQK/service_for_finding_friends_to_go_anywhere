package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Statistics;

import java.time.LocalDateTime;

public interface StatisticRepository extends JpaRepository<Statistics, Long> {

    @Query(nativeQuery = true,
            value = "SELECT app AS AN, COUNT(*) AS countAll, " +
                    "COUNT(DISTINCT ip) AS countUniqueIp FROM statistics " +
                    "WHERE time >= :start AND time <= :end AND uri = :uri " +
                    "GROUP BY app")
    Info getStatistics(LocalDateTime start, LocalDateTime end, String uri);
}