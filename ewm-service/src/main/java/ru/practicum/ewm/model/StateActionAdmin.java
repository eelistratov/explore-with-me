package ru.practicum.ewm.model;

/**
 * Действие администратора над событием.
 * <ul>
 *     <li>PUBLISH_EVENT — опубликовать событие (перевод из PENDING в PUBLISHED);</li>
 *     <li>REJECT_EVENT — отклонить событие (перевод в CANCELED).</li>
 * </ul>
 */
public enum StateActionAdmin {
    PUBLISH_EVENT,
    REJECT_EVENT
}