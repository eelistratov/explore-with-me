package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

import java.util.List;

/**
 * Сервис для работы с категориями.
 * Используется в admin и public API.
 */
public interface CategoryService {

    /**
     * Создание новой категории.
     * Имя категории должно быть уникальным.
     *
     * @param dto данные новой категории
     * @return созданная категория
     */
    CategoryDto create(NewCategoryDto dto);

    /**
     * Обновление категории.
     * Имя категории должно быть уникальным.
     *
     * @param catId id категории
     * @param dto   новые данные
     * @return обновлённая категория
     */
    CategoryDto update(Long catId, CategoryDto dto);

    /**
     * Удаление категории.
     * Нельзя удалить, если с категорией связано хотя бы одно событие.
     *
     * @param catId id категории
     */
    void delete(Long catId);

    /**
     * Список категорий с пагинацией.
     *
     * @param from количество пропускаемых записей
     * @param size количество записей в наборе
     * @return список категорий
     */
    List<CategoryDto> getAll(int from, int size);

    /**
     * Получение категории по id.
     *
     * @param catId id категории
     * @return категория
     */
    CategoryDto getById(Long catId);
}