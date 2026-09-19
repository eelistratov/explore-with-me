package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Подборка событий.
 * Соответствует схеме CompilationDto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompilationDto {

    private Long id;

    private String title;

    private Boolean pinned;

    @Builder.Default
    private Set<EventShortDto> events = new LinkedHashSet<>();
}