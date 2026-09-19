package ru.practicum.ewm.exception;

/**
 * Исключение, сигнализирующее о запрете операции.
 * Используется в ситуациях, когда пользователь не имеет прав
 * на выполнение действия (не инициатор, не автор и т.п.).
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}