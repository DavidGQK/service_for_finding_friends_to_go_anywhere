package ru.practicum.statistics.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.statistics.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HttpClient {
    HttpStatus addStatistics(String uri, String ip);

    ResponseEntity<List<ViewStats>> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris,
                                                  Boolean unique);
}