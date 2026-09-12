package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO для сохранения информации о том, что к эндпоинту был запрос.
 * Соответствует схеме EndpointHit из спецификации stats-сервиса.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EndpointHit {

    /**
     * Идентификатор записи. Заполняется сервером, клиент не передаёт.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    /**
     * Идентификатор сервиса, для которого записывается информация.
     * Например: "ewm-main-service".
     */
    @NotBlank
    private String app;

    /**
     * URI, для которого был осуществлён запрос. Например: "/events/1".
     */
    @NotBlank
    private String uri;

    /**
     * IP-адрес пользователя, осуществившего запрос.
     */
    @NotBlank
    private String ip;

    /**
     * Дата и время, когда был совершён запрос к эндпоинту.
     * Формат: "yyyy-MM-dd HH:mm:ss". Например: "2022-09-06 11:00:23".
     */
    @NotNull
    private String timestamp;
}