package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationRequest;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.stats.client.StatsClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса подборок событий.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;
    private final CompilationMapper compilationMapper;
    private final StatsClient statsClient;

    // Public API

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());

        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAllWithEvents(pageable);
        } else {
            compilations = compilationRepository.findAllByPinnedWithEvents(pinned, pageable);
        }

        if (compilations.isEmpty()) {
            return List.of();
        }

        List<Long> eventIds = compilations.stream()
                .flatMap(c -> c.getEvents().stream())
                .map(Event::getId)
                .distinct()
                .toList();

        Map<Long, Long> confirmed = getConfirmedRequests(eventIds);
        Map<Long, Long> views = getViewsForEvents(eventIds);

        return compilations.stream()
                .map(c -> compilationMapper.toDto(c, confirmed, views))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getById(Long compId) {
        Compilation compilation = compilationRepository.findByIdWithEvents(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        List<Long> eventIds = compilation.getEvents().stream()
                .map(Event::getId)
                .toList();
        Map<Long, Long> confirmed = getConfirmedRequests(eventIds);
        Map<Long, Long> views = getViewsForEvents(eventIds);

        return compilationMapper.toDto(compilation, confirmed, views);
    }

    // Admin API

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto dto) {
        if (compilationRepository.existsByTitle(dto.getTitle())) {
            throw new ConflictException("Compilation with title=" + dto.getTitle() + " already exists");
        }

        Set<Event> events = loadEvents(dto.getEvents());
        Compilation compilation = compilationMapper.toEntity(dto, events);
        Compilation saved = compilationRepository.save(compilation);

        log.debug("Создана подборка: id={}, title={}, events={}",
                saved.getId(), saved.getTitle(), saved.getEvents().size());

        Map<Long, Long> confirmed = getConfirmedRequests(saved.getEvents().stream().map(Event::getId).toList());
        Map<Long, Long> views = getViewsForEvents(saved.getEvents().stream().map(Event::getId).toList());
        return compilationMapper.toDto(saved, confirmed, views);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest dto) {
        Compilation compilation = compilationRepository.findByIdWithEvents(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        if (dto.getTitle() != null) {
            if (compilationRepository.existsByTitleAndIdNot(dto.getTitle(), compId)) {
                throw new ConflictException("Compilation with title=" + dto.getTitle() + " already exists");
            }
            compilation.setTitle(dto.getTitle());
        }

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }

        if (dto.getEvents() != null) {
            Set<Event> events = loadEvents(dto.getEvents());
            compilation.getEvents().clear();
            compilation.getEvents().addAll(events);
        }

        Compilation updated = compilationRepository.save(compilation);

        List<Long> eventIds = updated.getEvents().stream().map(Event::getId).toList();
        Map<Long, Long> confirmed = getConfirmedRequests(eventIds);
        Map<Long, Long> views = getViewsForEvents(eventIds);
        return compilationMapper.toDto(updated, confirmed, views);
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        compilationRepository.delete(compilation);
        log.debug("Удалена подборка: id={}", compId);
    }

    // Вспомогательные методы
    /**
     * Загрузка событий по id.
     * Если хотя бы одно событие не найдено — 404.
     */
    private Set<Event> loadEvents(Set<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return new LinkedHashSet<>();
        }
        List<Event> events = eventRepository.findAllById(eventIds);
        if (events.size() != eventIds.size()) {
            throw new NotFoundException("Some events were not found");
        }
        return new LinkedHashSet<>(events);
    }

    /**
     * Батч-подсчёт подтверждённых заявок для списка событий.
     */
    private Map<Long, Long> getConfirmedRequests(List<Long> eventIds) {
        if (eventIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Object[]> rows = requestRepository.countByEventIdsAndStatus(eventIds, RequestStatus.CONFIRMED);
        return rows.stream().collect(Collectors.toMap(
                row -> (Long) row[0],
                row -> (Long) row[1]
        ));
    }

    /**
     * Батч-просмотры для списка событий (один запрос к stats-сервису).
     */
    private Map<Long, Long> getViewsForEvents(List<Long> eventIds) {
        if (eventIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<String> uris = eventIds.stream()
                .map(id -> "/events/" + id)
                .toList();
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.now().plusYears(1);

        List<ViewStats> stats = statsClient.getStats(start, end, uris, true);
        return stats.stream().collect(Collectors.toMap(
                s -> parseEventIdFromUri(s.getUri()),
                ViewStats::getHits,
                (a, b) -> a
        ));
    }

    private Long parseEventIdFromUri(String uri) {
        String[] parts = uri.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}