package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.stats.model.EndpointHitEntity;
import ru.practicum.stats.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Реализация сервиса статистики.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EndpointHitRepository repository;

    @Override
    @Transactional
    public void saveHit(EndpointHit hit) {
        EndpointHitEntity entity = EndpointHitEntity.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(LocalDateTime.parse(hit.getTimestamp(), FORMATTER))
                .build();
        repository.save(entity);
        log.debug("Сохранён хит: app={}, uri={}, ip={}, timestamp={}",
                hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        boolean hasUris = uris != null && !uris.isEmpty();
        List<ViewStats> result;

        if (unique) {
            if (hasUris) {
                result = repository.findUniqueStatsByUris(start, end, uris);
            } else {
                result = repository.findUniqueStats(start, end);
            }
        } else {
            if (hasUris) {
                result = repository.findStatsByUris(start, end, uris);
            } else {
                result = repository.findStats(start, end);
            }
        }

        log.debug("Статистика получена: start={}, end={}, uris={}, unique={}, size={}",
                start, end, uris, unique, result.size());
        return result;
    }
}