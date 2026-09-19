package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.ParticipationRequest;

/**
 * Маппер для заявок на участие.
 * В DTO поля event и requester представлены их id, а не вложенными объектами.
 */
@Component
public class ParticipationRequestMapper {

    /**
     * Сущность → DTO.
     */
    public ParticipationRequestDto toDto(ParticipationRequest request) {
        if (request == null) {
            return null;
        }
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent() != null ? request.getEvent().getId() : null)
                .requester(request.getRequester() != null ? request.getRequester().getId() : null)
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}