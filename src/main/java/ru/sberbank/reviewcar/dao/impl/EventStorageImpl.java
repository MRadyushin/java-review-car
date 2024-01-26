package ru.sberbank.reviewcar.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.sberbank.reviewcar.model.Event;
import ru.sberbank.reviewcar.dao.mapper.*;
import ru.sberbank.reviewcar.dao.EventStorage;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class EventStorageImpl implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getFeedByUserId(int userId) {
        final String sql = "SELECT * FROM feed WHERE user_id = ? ";

        return jdbcTemplate.query(sql, new EventMapper(), userId);
    }

    @Override
    public void createEvent(Event event) {
        final String sql = "INSERT INTO feed (user_id, timestamp, event_type, operation, entity_id) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                event.getUserId(),
                event.getTimestamp(),
                event.getEventType().name(),
                event.getOperation().name(),
                event.getEntityId());
    }
}
