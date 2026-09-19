package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Category;

import java.util.List;

/**
 * Репозиторий для работы с категориями.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Проверка существования категории с указанным именем.
     */
    boolean existsByName(String name);

    /**
     * Проверка существования категории с указанным именем, кроме указанной.
     * Используется при обновлении: имя не должно совпадать с другими категориями.
     */
    boolean existsByNameAndIdNot(String name, Long id);

    /**
     * Постраничный список всех категорий.
     */
    List<Category> findAllBy(Pageable pageable);
}