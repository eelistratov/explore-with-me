package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.LocationDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UserShortDto;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;

/**
 * Маппер для событий.
 * Преобразует между сущностью Event и DTO.
 * Поля confirmedRequests и views заполняются снаружи — они вычисляемые.
 */
@Component
public class EventMapper {

    /**
     * Полная информация о событии.
     *
     * @param event             сущность события
     * @param confirmedRequests количество подтверждённых заявок (из БД)
     * @param views             количество просмотров (из stats-сервиса)
     */
    public EventFullDto toFullDto(Event event, Long confirmedRequests, Long views) {
        if (event == null) {
            return null;
        }
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests != null ? confirmedRequests : 0L)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(toUserShortDto(event.getInitiator()))
                .location(toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views != null ? views : 0L)
                .build();
    }

    /**
     * Краткая информация о событии.
     *
     * @param event             сущность события
     * @param confirmedRequests количество подтверждённых заявок
     * @param views             количество просмотров
     */
    public EventShortDto toShortDto(Event event, Long confirmedRequests, Long views) {
        if (event == null) {
            return null;
        }
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests != null ? confirmedRequests : 0L)
                .eventDate(event.getEventDate())
                .initiator(toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views != null ? views : 0L)
                .build();
    }

    /**
     * DTO создания → новая сущность.
     * id, initiator, state, createdOn, publishedOn устанавливаются в сервисе.
     *
     * @param dto        DTO нового события
     * @param category   уже загруженная категория
     * @param initiator  уже загруженный пользователь
     */
    public Event toEntity(NewEventDto dto, Category category, User initiator) {
        if (dto == null) {
            return null;
        }
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .title(dto.getTitle())
                .category(category)
                .initiator(initiator)
                .location(toLocation(dto.getLocation()))
                .eventDate(dto.getEventDate())
                .createdOn(LocalDateTime.now())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .state(EventState.PENDING)
                .build();
    }

    // ============================================================
    // Вспомогательные методы для вложенных DTO
    // ============================================================

    private CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private UserShortDto toUserShortDto(User user) {
        if (user == null) {
            return null;
        }
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    private LocationDto toLocationDto(Location location) {
        if (location == null) {
            return null;
        }
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    private Location toLocation(LocationDto dto) {
        if (dto == null) {
            return null;
        }
        return Location.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }
}