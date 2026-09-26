package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentRequest;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.CommentStatus;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса комментариев.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    // ============================================================
    // Private API
    // ============================================================

    @Override
    @Transactional
    public CommentDto create(Long userId, Long eventId, NewCommentDto dto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Cannot comment unpublished event");
        }

        Comment comment = Comment.builder()
                .text(dto.getText())
                .event(event)
                .author(author)
                .created(LocalDateTime.now())
                .status(CommentStatus.PUBLISHED)
                .build();

        Comment saved = commentRepository.save(comment);
        log.debug("Создан комментарий: id={}, eventId={}, authorId={}",
                saved.getId(), eventId, userId);
        return commentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CommentDto update(Long userId, Long commentId, UpdateCommentRequest dto) {
        Comment comment = commentRepository.findByIdAndAuthorIdAndStatus(
                        commentId, userId, CommentStatus.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        comment.setText(dto.getText());
        comment.setUpdated(LocalDateTime.now());

        Comment updated = commentRepository.save(comment);
        log.debug("Обновлён комментарий: id={}", commentId);
        return commentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteByAuthor(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndAuthorIdAndStatus(
                        commentId, userId, CommentStatus.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        comment.setStatus(CommentStatus.DELETED);
        commentRepository.save(comment);
        log.debug("Удалён комментарий автором: id={}", commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getUserComments(Long userId) {
        checkUserExists(userId);
        return commentRepository.findPublishedByAuthorId(userId, CommentStatus.PUBLISHED).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    // ============================================================
    // Public API
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getEventComments(Long eventId, int from, int size) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        return commentRepository.findPublishedByEventId(eventId, CommentStatus.PUBLISHED, pageable)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    // ============================================================
    // Admin API
    // ============================================================

    @Override
    @Transactional
    public void deleteByAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        comment.setStatus(CommentStatus.DELETED);
        commentRepository.save(comment);
        log.debug("Удалён комментарий админом: id={}", commentId);
    }

    // ============================================================
    // Вспомогательные методы
    // ============================================================

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }
}