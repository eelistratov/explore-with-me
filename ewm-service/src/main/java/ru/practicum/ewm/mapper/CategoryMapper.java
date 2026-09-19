package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.model.Category;

/**
 * Маппер для категорий.
 * Преобразует между сущностью Category и DTO.
 */
@Component
public class CategoryMapper {

    /**
     * Сущность → DTO (ответ API).
     */
    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    /**
     * DTO создания → сущность.
     * id не устанавливается — его присвоит БД.
     */
    public Category toEntity(NewCategoryDto dto) {
        if (dto == null) {
            return null;
        }
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    /**
     * Обновление существующей сущности из DTO.
     * Используется в PATCH /admin/categories/{catId}.
     */
    public void updateEntity(Category category, CategoryDto dto) {
        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
    }
}