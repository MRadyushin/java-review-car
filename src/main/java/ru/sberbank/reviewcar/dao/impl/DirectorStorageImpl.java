package ru.sberbank.reviewcar.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.sberbank.reviewcar.exception.DirectorException;
import ru.sberbank.reviewcar.model.Director;
import ru.sberbank.reviewcar.dao.DirectorStorage;
import ru.sberbank.reviewcar.dao.mapper.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class DirectorStorageImpl implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;
    private final DirectorMapper directorMapper;

    /**
     * класс констант-запросов
     */
    private static class DirectorSql {
        static final String SELECT_DIRECTORS_ALL = "SELECT * FROM directors";
        static final String SELECT_DIRECTORS_FIND_BY_ID = "SELECT * FROM directors WHERE id = ?";
        static final String UPDATE_DIRECTORS = "UPDATE directors SET name = ? WHERE id = ?";
        static final String DELETE_DIRECTORS = "DELETE FROM directors WHERE id = ?";

    }

    /**
     *
     * @return возвращает всех производителей
     */

    @Override
    public Collection<Director> findAll() {
        try {
            return jdbcTemplate.query(DirectorSql.SELECT_DIRECTORS_ALL, directorMapper);
        } catch (DataAccessException ex) {
            return Collections.emptyList();
        }
    }

    /**
     *
     * @param directorId - id производителя
     * @return возвращает производителя по id
     */
    @Override
    public Optional<Director> findById(Integer directorId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            DirectorSql.SELECT_DIRECTORS_FIND_BY_ID, directorMapper, directorId));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    /**
     *
     * создание произврдителя
     */
    @Override
    public Director save(Director director) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource("name", director.getName());

            final KeyHolder kh = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("directors")
                    .usingGeneratedKeyColumns("id")
                    .executeAndReturnKeyHolder(parameters);

            final int addedDirectorId = kh.getKey().intValue();

            return Director.builder().id(addedDirectorId).name(director.getName()).build();

        } catch (DataAccessException | NullPointerException ex) {
            String message = String.format("Ошибка сохранения директора: %s", director.getName());
            throw new DirectorException(message);
        }
    }

    /**
     *
     * изменение производителя
     */
    @Override
    public Director update(Director director) {
        try {
            jdbcTemplate.update(DirectorSql.UPDATE_DIRECTORS, director.getName(), director.getId());
        } catch (DataAccessException ex) {
            String message =
                    String.format(
                            "Ошибка обновления директора: %s, %s", director.getId(), director.getName());
            throw new DirectorException(message);
        }
        return director;
    }

    /**
     * удаление производителя
     * @param directorId - id производителя
     */
    @Override
    public void remove(Integer directorId) {
        try {
            jdbcTemplate.update(DirectorSql.DELETE_DIRECTORS, directorId);
        } catch (DataAccessException ex) {
            String message = String.format("Ошибка удаления директора: %s", directorId);
            throw new DirectorException(message);
        }
    }

    /**
     *
     * @param carId - id авто
     * @return возвращается производитель для опред авто
     */
    public List<Director> getDirectorsByCarId(Integer carId) {
        String statement = "SELECT * FROM directors AS d " +
                "LEFT JOIN cars_directors AS fd " +
                "ON d.id = fd.director_id " +
                "WHERE fd.car_id = ?";
        return jdbcTemplate.query(statement, this::makeDirector, carId);
    }

    /**
     *
     * создатель производителя
     */
    private Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder().id(rs.getInt("id")).name(rs.getString("name")).build();
    }


}
