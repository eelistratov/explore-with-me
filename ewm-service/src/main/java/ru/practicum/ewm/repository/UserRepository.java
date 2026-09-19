package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.User;

import java.util.List;

/**
 * Репозиторий для работы с пользователями.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Проверка существования пользователя с указанным email.
     */
    boolean existsByEmail(String email);

    /**
     * Поиск пользователей по списку id.
     */
    List<User> findAllByIdIn(List<Long> ids);

    /**
     * Поиск пользователей по списку id с пагинацией.
     */
    List<User> findAllByIdIn(List<Long> ids, Pageable pageable);

    /**
     * Постраничный список всех пользователей.
     */
    List<User> findAllBy(Pageable pageable);
}