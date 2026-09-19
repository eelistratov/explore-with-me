package ru.practicum.stats.service;

import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис статистики: сохранение обращений и выдача агрегированной статистики.
 */
public interface StatsService {

    /**
     * Сохранить информацию об обращении к эндпоинту.
     *
     * @param hit DTO с данными обращения
     */
    void saveHit(EndpointHit hit);

    /**
     * Получить статистику за диапазон дат.
     *
     * @param start  начало диапазона (включительно)
     * @param end    конец диапазона (включительно)
     * @param uris   список URI для фильтрации; если {@code null} или пустой — фильтр не применяется
     * @param unique учитывать только уникальные обращения (по IP)
     * @return список агрегированной статистики
     */
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}