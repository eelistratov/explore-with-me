package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Compilation;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с подборками событий.
 */
@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    /**
     * Проверка существования подборки с указанным заголовком.
     */
    boolean existsByTitle(String title);

    /**
     * Проверка существования подборки с указанным заголовком, кроме указанной.
     * Используется при обновлении.
     */
    boolean existsByTitleAndIdNot(String title, Long id);

    /**
     * Постраничный список всех подборок.
     */
    List<Compilation> findAllBy(Pageable pageable);

    /**
     * Постраничный список подборок с фильтром по pinned.
     */
    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);

    /**
     * Поиск подборки по id с загрузкой событий одним запросом.
     */
    @Query("""
            SELECT DISTINCT c FROM Compilation c
            LEFT JOIN FETCH c.events
            WHERE c.id = :id
            """)
    Optional<Compilation> findByIdWithEvents(@Param("id") Long id);

    /**
     * Постраничный список подборок с загрузкой событий.
     * Используется для GET /compilations — чтобы не было N+1.
     */
    @Query("""
            SELECT DISTINCT c FROM Compilation c
            LEFT JOIN FETCH c.events
            """)
    List<Compilation> findAllWithEvents(Pageable pageable);

    /**
     * Постраничный список подборок с фильтром pinned и загрузкой событий.
     */
    @Query("""
            SELECT DISTINCT c FROM Compilation c
            LEFT JOIN FETCH c.events
            WHERE c.pinned = :pinned
            """)
    List<Compilation> findAllByPinnedWithEvents(@Param("pinned") Boolean pinned, Pageable pageable);
}