package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.CommentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с комментариями.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Комментарии к событию со статусом PUBLISHED (публичный список).
     * С JOIN FETCH автора, чтобы избежать N+1.
     */
    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.author
            WHERE c.event.id = :eventId AND c.status = :status
            ORDER BY c.created ASC
            """)
    List<Comment> findPublishedByEventId(@Param("eventId") Long eventId,
                                         @Param("status") CommentStatus status,
                                         Pageable pageable);

    /**
     * Комментарии пользователя (все статусы, кроме DELETED — свои удалённые он тоже не видит).
     */
    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.author
            WHERE c.author.id = :authorId AND c.status = :status
            ORDER BY c.created DESC
            """)
    List<Comment> findPublishedByAuthorId(@Param("authorId") Long authorId,
                                          @Param("status") CommentStatus status);

    /**
     * Поиск комментария по id со статусом PUBLISHED (для публичных операций).
     */
    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.author
            JOIN FETCH c.event
            WHERE c.id = :id AND c.status = :status
            """)
    Optional<Comment> findByIdAndStatus(@Param("id") Long id,
                                        @Param("status") CommentStatus status);

    /**
     * Поиск комментария по id и автору (для операций пользователя).
     */
    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.author
            JOIN FETCH c.event
            WHERE c.id = :id AND c.author.id = :authorId AND c.status = :status
            """)
    Optional<Comment> findByIdAndAuthorIdAndStatus(@Param("id") Long id,
                                                   @Param("authorId") Long authorId,
                                                   @Param("status") CommentStatus status);

    /**
     * Количество PUBLISHED-комментариев к событию.
     */
    long countByEventIdAndStatus(Long eventId, CommentStatus status);

    /**
     * подсчёт PUBLISHED-комментариев для списка событий.
     * Возвращает [eventId, count]. Один запрос вместо N — против N+1.
     */
    @Query("""
            SELECT c.event.id, COUNT(c.id)
            FROM Comment c
            WHERE c.event.id IN :eventIds AND c.status = :status
            GROUP BY c.event.id
            """)
    List<Object[]> countByEventIdsAndStatus(@Param("eventIds") List<Long> eventIds,
                                            @Param("status") CommentStatus status);
}