package ru.sberbank.reviewcar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sberbank.reviewcar.dao.eventEnum.EventOperation;
import ru.sberbank.reviewcar.dao.eventEnum.EventType;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private Integer eventId;
    private Long timestamp;
    private Integer userId;
    private EventType eventType;
    private EventOperation operation;
    private Integer entityId;
}