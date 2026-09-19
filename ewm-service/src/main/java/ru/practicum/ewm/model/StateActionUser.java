package ru.practicum.ewm.model;

/**
 * Действие инициатора над своим событием.
 * <ul>
 *     <li>SEND_TO_REVIEW — отправить событие на модерацию
 *         (перевод из CANCELED в PENDING);</li>
 *     <li>CANCEL_REVIEW — отменить своё событие
 *         (перевод из PENDING в CANCELED).</li>
 * </ul>
 */
public enum StateActionUser {
    SEND_TO_REVIEW,
    CANCEL_REVIEW
}