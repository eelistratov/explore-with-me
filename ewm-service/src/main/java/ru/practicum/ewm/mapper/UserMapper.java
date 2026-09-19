package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.dto.UserShortDto;
import ru.practicum.ewm.model.User;

/**
 * Маппер для пользователей.
 * Преобразует между сущностью User и DTO.
 */
@Component
public class UserMapper {

    /**
     * Сущность → полный DTO (для admin API).
     */
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Сущность → краткий DTO (для вложенного использования).
     */
    public UserShortDto toShortDto(User user) {
        if (user == null) {
            return null;
        }
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    /**
     * DTO создания → сущность.
     * id не устанавливается — его присвоит БД.
     */
    public User toEntity(NewUserRequest dto) {
        if (dto == null) {
            return null;
        }
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}