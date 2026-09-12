package ru.practicum.stats.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений сервиса статистики.
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException e) {
        String description = e.getBindingResult().getFieldErrors().stream()
                .map(err -> "Field: " + err.getField() + ". Error: " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.debug("Ошибка валидации: {}", description);
        return new ErrorResponse("Validation error", description);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String description = "Parameter: " + e.getName() + ". Value: " + e.getValue()
                + ". Required type: " + (e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");
        log.debug("Ошибка типа параметра: {}", description);
        return new ErrorResponse("Bad request", description);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParam(MissingServletRequestParameterException e) {
        String description = "Missing parameter: " + e.getParameterName();
        log.debug("Отсутствует параметр: {}", description);
        return new ErrorResponse("Bad request", description);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOther(Exception e) {
        log.error("Непредвиденная ошибка", e);
        return new ErrorResponse("Internal server error", e.getMessage());
    }
}