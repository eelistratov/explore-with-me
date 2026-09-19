package ru.practicum.ewm.exception;

/**
 * Исключение, сигнализирующее о том, что запрашиваемый объект не найден.
 * Соответствует HTTP-статусу 404 NOT_FOUND.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}