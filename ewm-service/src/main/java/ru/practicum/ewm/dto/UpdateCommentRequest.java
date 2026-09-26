package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO для редактирования комментария.
 * Используется в PATCH /users/{userId}/comments/{commentId}.
 * Текст комментария обязателен — частичное обновление не поддерживается.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCommentRequest {

    @NotBlank
    @Size(min = 1, max = 2000)
    private String text;
}