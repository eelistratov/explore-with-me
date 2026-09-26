package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentRequest;

import java.util.List;

/**
 * Сервис для работы с комментариями.
 * Покрывает public, private и admin части API.
 */
public interface CommentService {

    // ============================================================
    // Private API
    // ============================================================

    /**
     * Создание комментария к событию.
     * Комментировать можно только опубликованные события.
     *
     * @param userId  id автора
     * @param eventId id события
     * @param dto     текст комментария
     * @return созданный комментарий
     */
    CommentDto create(Long userId, Long eventId, NewCommentDto dto);

    /**
     * Редактирование своего комментария.
     * Без ограничения по времени.
     *
     * @param userId    id автора
     * @param commentId id комментария
     * @param dto       новый текст
     * @return обновлённый комментарий
     */
    CommentDto update(Long userId, Long commentId, UpdateCommentRequest dto);

    /**
     * Удаление своего комментария (soft delete).
     *
     * @param userId    id автора
     * @param commentId id комментария
     */
    void deleteByAuthor(Long userId, Long commentId);

    /**
     * Список своих комментариев.
     *
     * @param userId id автора
     * @return список комментариев
     */
    List<CommentDto> getUserComments(Long userId);

    // ============================================================
    // Public API
    // ============================================================

    /**
     * Публичный список комментариев к событию.
     * Возвращаются только PUBLISHED, с пагинацией.
     *
     * @param eventId id события
     * @param from    пропуск записей
     * @param size    размер набора
     * @return список комментариев
     */
    List<CommentDto> getEventComments(Long eventId, int from, int size);

    // ============================================================
    // Admin API
    // ============================================================

    /**
     * Удаление комментария администратором (soft delete).
     *
     * @param commentId id комментария
     */
    void deleteByAdmin(Long commentId);
}