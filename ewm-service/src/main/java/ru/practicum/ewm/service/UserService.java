package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Используется только в admin API.
 */
public interface UserService {

    /**
     * Регистрация нового пользователя.
     * Email должен быть уникальным.
     *
     * @param dto данные нового пользователя
     * @return созданный пользователь
     */
    UserDto create(NewUserRequest dto);

    /**
     * Удаление пользователя по id.
     *
     * @param userId id пользователя
     */
    void delete(Long userId);

    /**
     * Получение списка пользователей с фильтрацией по id и пагинацией.
     *
     * @param ids  список id (может быть {@code null} или пустым — тогда все)
     * @param from количество пропускаемых записей
     * @param size количество записей в наборе
     * @return список пользователей
     */
    List<UserDto> getAll(List<Long> ids, int from, int size);
}