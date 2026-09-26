package ru.practicum.ewm.model;

/**
 * Статус комментария.
 * <ul>
 *     <li>PUBLISHED — комментарий опубликован и виден всем;</li>
 *     <li>DELETED — комментарий удалён (soft delete) и не отображается.</li>
 * </ul>
 */
public enum CommentStatus {
    PUBLISHED,
    DELETED
}