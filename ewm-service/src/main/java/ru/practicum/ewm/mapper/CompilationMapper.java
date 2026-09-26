package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Маппер для подборок событий.
 * Заполняет вложенный список событий в краткой форме (EventShortDto).
 */
@Component
public class CompilationMapper {

    private final EventMapper eventMapper;

    public CompilationMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    /**
     * Сущность → DTO.
     *
     * @param compilation       сущность подборки
     * @param confirmedRequests Map: eventId → количество подтверждённых заявок
     * @param views             Map: eventId → количество просмотров
     * @param commentsCounts    Map: eventId → количество комментариев
     */
    public CompilationDto toDto(Compilation compilation,
                                Map<Long, Long> confirmedRequests,
                                Map<Long, Long> views,
                                Map<Long, Long> commentsCounts) {
        if (compilation == null) {
            return null;
        }
        Set<EventShortDto> events = compilation.getEvents().stream()
                .map(event -> eventMapper.toShortDto(
                        event,
                        confirmedRequests.getOrDefault(event.getId(), 0L),
                        views.getOrDefault(event.getId(), 0L),
                        commentsCounts.getOrDefault(event.getId(), 0L)
                ))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(events)
                .build();
    }

    /**
     * DTO создания → новая сущность.
     * Список событий устанавливается в сервисе: маппер не ходит в БД.
     */
    public Compilation toEntity(NewCompilationDto dto, Set<Event> events) {
        if (dto == null) {
            return null;
        }
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned() != null ? dto.getPinned() : false)
                .events(events != null ? events : new LinkedHashSet<>())
                .build();
    }
}