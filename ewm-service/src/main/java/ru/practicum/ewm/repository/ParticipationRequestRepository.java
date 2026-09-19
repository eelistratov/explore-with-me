package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.RequestStatus;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с заявками на участие в событиях.
 */
@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    /**
     * Все заявки пользователя на чужие события.
     */
    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    /**
     * Все заявки на событие.
     */
    List<ParticipationRequest> findAllByEventId(Long eventId);

    /**
     * Все заявки на событие с указанным статусом.
     */
    List<ParticipationRequest> findAllByEventIdAndStatus(Long eventId, RequestStatus status);

    /**
     * Все заявки на событие по списку id.
     */
    List<ParticipationRequest> findAllByIdInAndEventId(List<Long> ids, Long eventId);

    /**
     * Заявка по id и id пользователя (для проверки принадлежности).
     */
    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);

    /**
     * Проверка, что пользователь уже подал заявку на событие.
     */
    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    /**
     * Количество подтверждённых заявок на событие.
     */
    long countByEventIdAndStatus(Long eventId, RequestStatus status);

    /**
     * Количество подтверждённых заявок для списка событий (для батч-подсчёта).
     */
    @Query("""
            SELECT r.event.id, COUNT(r.id)
            FROM ParticipationRequest r
            WHERE r.event.id IN :eventIds AND r.status = :status
            GROUP BY r.event.id
            """)
    List<Object[]> countByEventIdsAndStatus(@Param("eventIds") List<Long> eventIds,
                                            @Param("status") RequestStatus status);
}