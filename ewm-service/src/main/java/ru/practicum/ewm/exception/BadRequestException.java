package ru.practicum.ewm.exception;

/**
 * Исключение, сигнализирующее о некорректно составленном запросе.
 * Соответствует HTTP-статусу 400 BAD_REQUEST.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}