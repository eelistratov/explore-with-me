package ru.practicum.ewm.model;

/**
 * Статус заявки на участие в событии.
 * <ul>
 *     <li>PENDING — ожидает подтверждения инициатором;</li>
 *     <li>CONFIRMED — подтверждена инициатором;</li>
 *     <li>REJECTED — отклонена инициатором (или автоматически при достижении лимита);</li>
 *     <li>CANCELED — отменена самим участником.</li>
 * </ul>
 */
public enum RequestStatus {
    PENDING,
    CONFIRMED,
    REJECTED,
    CANCELED
}