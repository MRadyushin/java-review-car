package ru.sberbank.reviewcar.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.sberbank.reviewcar.model.Car;
import ru.sberbank.reviewcar.model.Director;
import ru.sberbank.reviewcar.model.Klass;
import ru.sberbank.reviewcar.model.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarRowMapper implements RowMapper<Car> {
    /**
     * маппингавто
     * @param rs the {@code ResultSet} to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return
     * @throws SQLException
     * JdbcTemplate вызывает метод mapRow для каждой строки в наборе результатов и передает ее номер строки в качестве аргумента.
     * Метод возвращает объект определенного типа
     */
    @Override
    public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Type> types = new ArrayList<>();
        String typesString = rs.getString("types");
        if (typesString != null) {
            String[] typeStrings = typesString.split(";");
            for (String typeString : typeStrings) {
                String[] typeData = typeString.split(",");
                try {
                    final Type type = Type.builder()
                        .id(Integer.parseInt(typeData[0]))
                        .name(typeData[1])
                        .build();
                    types.add(type);
                } catch (NumberFormatException e) {
                    break;
                }
            }
        }
        List<Director> directors = new ArrayList<>();
        String directorsString = rs.getString("directors");
        if (directorsString != null) {
            String[] directorStrings = directorsString.split(";");
            for (String directorString : directorStrings) {
                String[] directorData = directorString.split(",");
                try {
                    final Director director = Director.builder()
                        .id(Integer.parseInt(directorData[0]))
                        .name(directorData[1])
                        .build();
                    directors.add(director);
                } catch (NumberFormatException e) {
                    break;
                }
            }
        }

        return Car.builder()
            .id(rs.getInt("car_id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .releaseDate(rs.getDate("releasedate").toLocalDate())
            .price(rs.getInt("price"))
            .rate(rs.getInt("likes_count"))
            .klass(Klass.builder().id(rs.getInt("klass_id")).name(rs.getString("klass")).build())
            .types(types)
            .directors(directors)
            .build();
    }
}
