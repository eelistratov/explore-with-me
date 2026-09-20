package ru.practicum.ewm.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с событиями.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    /**
     * Проверка существования события в категории.
     * Используется при удалении категории.
     */
    boolean existsByCategoryId(Long categoryId);

    /**
     * Проверка, что событие принадлежит пользователю.
     */
    boolean existsByIdAndInitiatorId(Long eventId, Long userId);

    /**
     * Список событий, созданных пользователем, с пагинацией.
     */
    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    /**
     * Список событий, созданных пользователем, с конкретным состоянием.
     */
    List<Event> findAllByInitiatorIdAndState(Long userId, EventState state);

    /**
     * Поиск события по id с подгрузкой связанных сущностей.
     */
    @Query("""
            SELECT e FROM Event e
            JOIN FETCH e.category
            JOIN FETCH e.initiator
            WHERE e.id = :id
            """)
    Optional<Event> findByIdWithDetails(@Param("id") Long id);

    /**
     * Поиск события по id с пессимистической блокировкой (SELECT ... FOR UPDATE).
     * Используется при работе с заявками на участие, чтобы избежать гонки условий
     * при проверке лимита участников.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.id = :id")
    Optional<Event> findByIdWithLock(@Param("id") Long id);
}