package ru.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO для выгрузки статистики по одному эндпоинту.
 * Соответствует схеме ViewStats из спецификации stats-сервиса.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ViewStats {

    /**
     * Название сервиса. Например: "ewm-main-service".
     */
    private String app;

    /**
     * URI сервиса. Например: "/events/1".
     */
    private String uri;

    /**
     * Количество просмотров.
     */
    private Long hits;
}