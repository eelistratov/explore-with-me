package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Изменение статуса запроса на участие в событии текущего пользователя.
 * Соответствует схеме EventRequestStatusUpdateRequest.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private RequestUpdateStatus status;

    /**
     * Допустимые новые статусы заявок.
     * Отдельный enum только для этого DTO: по спецификации допустимы
     * только CONFIRMED и REJECTED.
     */
    public enum RequestUpdateStatus {
        CONFIRMED,
        REJECTED
    }
}