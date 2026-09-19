package ru.practicum.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа приложения сервиса статистики Explore With Me.
 */
@SpringBootApplication
public class StatsServerApp {

    public static void main(String[] args) {
        SpringApplication.run(StatsServerApp.class, args);
    }
}