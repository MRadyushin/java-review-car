package ru.sberbank.reviewcar.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.sberbank.reviewcar.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DirectorMapper implements RowMapper<Director> {
  @Override
  public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
    return Director.builder()
            .id(rs.getInt("id"))
            .name(rs.getString("name"))
            .build();
  }
}
