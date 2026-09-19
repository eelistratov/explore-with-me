package ru.practicum.ewm.exception;

/**
 * Исключение, сигнализирующее о нарушении бизнес-правила или целостности данных.
 * Соответствует HTTP-статусу 409 CONFLICT.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}