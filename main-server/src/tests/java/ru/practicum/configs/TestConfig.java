package ru.practicum.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.statistics.service.StatisticService;

@Configuration
public class TestConfig {

    @Bean
    public StatisticService StatisticService() {
        return new StatisticServiceConfig();
    }
}