package ru.sberbank.reviewcar.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.sberbank.reviewcar.dao.eventEnum.EventOperation;
import ru.sberbank.reviewcar.model.Event;
import ru.sberbank.reviewcar.dao.eventEnum.EventType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventMapper implements RowMapper<Event> {
    /**
     * маппинг действий
     * @param rs the {@code ResultSet} to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return
     * @throws SQLException
     * JdbcTemplate вызывает метод mapRow для каждой строки в наборе результатов и передает ее номер строки в качестве аргумента.
     * Метод возвращает объект определенного типа
     */
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
