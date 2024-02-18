package ru.sberbank.reviewcar.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.sberbank.reviewcar.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DirectorMapper implements RowMapper<Director> {
  /**
   * маппинг производителя
   * @param rs the {@code ResultSet} to map (pre-initialized for the current row)
   * @param rowNum the number of the current row
   * @return
   * @throws SQLException
   * JdbcTemplate вызывает метод mapRow для каждой строки в наборе результатов и передает ее номер строки в качестве аргумента.
   * Метод возвращает объект определенного типа
   */
  @Override
  public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
    return Director.builder()
            .id(rs.getInt("id"))
            .name(rs.getString("name"))
            .build();
  }
}
