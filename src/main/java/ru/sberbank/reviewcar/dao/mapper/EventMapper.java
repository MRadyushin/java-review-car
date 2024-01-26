package ru.sberbank.reviewcar.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.sberbank.reviewcar.dao.eventEnum.EventOperation;
import ru.sberbank.reviewcar.model.Event;
import ru.sberbank.reviewcar.dao.eventEnum.EventType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getInt("event_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(EventOperation.valueOf(rs.getString("operation")))
                .timestamp(rs.getLong("timestamp"))
                .userId(rs.getInt("user_id"))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}
