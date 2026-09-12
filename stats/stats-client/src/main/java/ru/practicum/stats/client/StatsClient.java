package ru.practicum.stats.client;

import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

/**
 * HTTP-клиент для взаимодействия с сервисом статистики.
 */
public interface StatsClient {

    /**
     * Отправить информацию об обращении к эндпоинту в сервис статистики.
     *
     * @param hit DTO с данными обращения
     */
    void hit(EndpointHit hit);

    /**
     * Получить статистику за диапазон дат.
     *
     * @param start  начало диапазона
     * @param end    конец диапазона
     * @param uris   список URI для фильтрации; может быть {@code null}
     * @param unique учитывать только уникальные обращения
     * @return список агрегированной статистики
     */
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}