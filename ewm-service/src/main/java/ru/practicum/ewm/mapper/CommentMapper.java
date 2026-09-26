package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.UserShortDto;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.User;

/**
 * Маппер для комментариев.
 * Преобразует сущность Comment в DTO CommentDto.
 */
@Component
public class CommentMapper {

    /**
     * Сущность → DTO.
     * Поле event представлено id события, поле author — краткая форма пользователя.
     */
    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(comment.getEvent() != null ? comment.getEvent().getId() : null)
                .author(toUserShortDto(comment.getAuthor()))
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .build();
    }

    /**
     * Пользователь → краткая форма.
     * Используется для поля author.
     */
    private UserShortDto toUserShortDto(User user) {
        if (user == null) {
            return null;
        }
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}