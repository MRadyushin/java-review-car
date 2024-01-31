package ru.sberbank.reviewcar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.dao.eventEnum.EventOperation;
import ru.sberbank.reviewcar.dao.eventEnum.EventType;
import ru.sberbank.reviewcar.dao.impl.UserStorageImpl;
import ru.sberbank.reviewcar.model.Event;
import ru.sberbank.reviewcar.dao.EventStorage;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventStorage eventStorage;
    private final UserStorageImpl userDbStorage;

    public List<Event> getFeed(int userId) {
        if (userDbStorage.getUser(userId) != null) {
            return eventStorage.getFeedByUserId(userId);
        } else {
            throw new NotFoundException("Not found");
        }
    }

    public void createEvent(int userId, EventType eventType, EventOperation eventOperation, int entityId) {
        Event event = Event.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(eventType)
                .operation(eventOperation)
                .entityId(entityId)
                .build();

        eventStorage.createEvent(event);
    }
}
