package ru.sberbank.reviewcar.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.model.Car;
import ru.sberbank.reviewcar.model.Director;
import ru.sberbank.reviewcar.model.Klass;
import ru.sberbank.reviewcar.model.Type;
import ru.sberbank.reviewcar.dao.CarStorage;
import ru.sberbank.reviewcar.dao.mapper.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
@Primary
@Slf4j
public class CarStorageImpl implements CarStorage {

    private final JdbcTemplate jdbcTemplate;

    private final DirectorMapper directorMapper;
    private final CarRowMapper carRowMapper;

    private final DirectorStorageImpl directorStorage;

    @Override
    public List<Car> getCar(List<Integer> id) {
        String inSql = String.join(",", Collections.nCopies(id.size(), "?"));
        return new ArrayList<>(Objects.requireNonNull(jdbcTemplate.query(String.format("SELECT " +
            "f.CAR_ID as carId, " +
            "f.NAME as name, " +
            "f.DESCRIPTION as description, " +
            "f.RATE as rate, " +
            "f.RELEASEDATE as releaseDate, " +
            "f.PRICE as price, " +
            "m.KLASS_ID AS klassId, " +
            "m.KLASS_NAME AS klassName, " +
            "g.TYPE_ID as typeId, " +
            "g.TYPE_NAME AS typeName " +
            "FROM " +
            "CARS f " +
            "LEFT JOIN KLASS m ON m.KLASS_ID = f.KLASS_ID " +
            "LEFT JOIN CAR_TYPES fg ON fg.CAR_ID = f.CAR_ID " +
            "LEFT JOIN TYPES g ON g.TYPE_ID = fg.TYPE_ID " +
            "WHERE f.CAR_ID IN (%s)", inSql), id.toArray(), this::makeCar)));
    }

    @Override
    public Collection<Car> getAllCars() {

        return jdbcTemplate.query("SELECT " +
            "f.car_id AS carid, " +
            "f.name AS name, " +
            "f.description AS description, " +
            "f.rate AS rate, " +
            "f.releasedate AS releasedate, " +
            "f.price AS price, " +
            "m.klass_id AS klassid, " +
            "m.klass_name AS klassname, " +
            "g.type_id AS typeid, " +
            "g.type_name AS typename " +
            "FROM " +
            "cars f " +
            "LEFT JOIN klass m ON m.klass_id = f.klass_id " +
            "LEFT JOIN car_types fg ON fg.car_id = f.car_id " +
            "LEFT JOIN types g ON g.type_id = fg.type_id", this::makeCar);
    }

    @Override
    public Car addCar(Car car) {
        String sqlInsertCar =
            "INSERT INTO cars (name, description, releasedate, price, rate, klass_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            connection -> {
                PreparedStatement ps =
                    connection.prepareStatement(sqlInsertCar, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, car.getName());
                ps.setString(2, car.getDescription());
                ps.setDate(3, java.sql.Date.valueOf(car.getReleaseDate()));
                ps.setInt(4, car.getPrice());
                ps.setInt(5, car.getRate() != null ? car.getRate() : 0);
                ps.setInt(6, car.getKlass().getId());
                return ps;
            },
            keyHolder);
        int carId = keyHolder.getKey().intValue();
        car.setId(carId);

        return car;
    }

    @Override
    public Car updateCar(Car car) {
        String sqlUpdateCar =
            "UPDATE cars SET name = ?, description = ?, releasedate = ?, price = ?, rate = ?, klass_id = ? WHERE car_id = ?";
        jdbcTemplate.update(
            sqlUpdateCar,
            car.getName(),
            car.getDescription(),
            java.sql.Date.valueOf(car.getReleaseDate()),
            car.getPrice(),
            car.getRate() != null ? car.getRate() : null,
            car.getKlass() != null ? car.getKlass().getId() : null,
            car.getId());

        return car;
    }

    @Override
    public String deleteCar(int carId) {
        try {
            String sqlQuery = "DELETE FROM cars WHERE car_id =?";
            jdbcTemplate.update(sqlQuery, carId);
            log.info("Машина" + carId + " удалена");
        } catch (DataAccessException e) {
            log.info("Машина" + carId + " не удалена / не найдена");
            throw new NotFoundException("Машина с id " + carId + " не найдена");
        }
        return "Машина с id " + carId + " удалён";
    }

    @Override
    public void makeLike(int idCar, int idUser) {
        String sqlInsertLikes = "INSERT INTO likes (car_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlInsertLikes, idCar, idUser);
        log.info("User с id=" + idUser + " поставил лайк car с id = " + idCar);
    }

    @Override
    public void deleteLike(int idCar, int idUser) {
        String sqlDelete = "DELETE FROM likes WHERE car_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlDelete, idCar, idUser);
        log.info("User с id=" + idUser + " удалил свой лайк car с id=" + idCar);
    }

    @Override
    public Collection<Car> getPopularCars(int count) {
        String sqlPopular = "SELECT f.car_id AS carid, f.name AS name, f.description AS description, " +
            "f.releasedate AS releasedate, f.price AS price, m.klass_id AS klassid, m.klass_name AS klassname, " +
            "g.type_id AS typeid, g.type_name AS typename, COUNT(l.user_id) AS rate " +
            "FROM cars f " +
            "LEFT OUTER JOIN car_types fg ON fg.car_id = f.car_id " +
            "LEFT OUTER JOIN types g ON g.type_id = fg.type_id " +
            "LEFT OUTER JOIN klass m ON m.klass_id = f.klass_id " +
            "LEFT JOIN likes l ON f.car_id = l.car_id " +
            "WHERE f.car_id IN ( " +
            "SELECT car_id AS carid FROM likes GROUP BY carid ORDER BY COUNT(user_id) DESC LIMIT ? ) " +
            "GROUP BY carid, name, description, releasedate, price, klassid, klassname, typeid, typename " +
            "ORDER BY rate DESC";

        Collection<Car> popularCars = jdbcTemplate.query(sqlPopular, this::makeCar, count);

        if (popularCars != null && popularCars.isEmpty()) {
            return getCar(IntStream.rangeClosed(1, count)
                .boxed()
                .collect(Collectors.toList()));
        }
        return popularCars;
    }

    @Override
    public List<Integer> getCarLikes(Integer id) {
        String sqlCarLikesById = "SELECT * FROM likes WHERE car_id = ?";
        return jdbcTemplate.query(sqlCarLikesById, (rs, rowNum) -> rs.getInt("user_id"), id);
    }

    @Override
    public Klass checkKlass(Car car) {
        Integer klassId = car.getKlass().getId();
        String sqlKlass = "SELECT * FROM klass WHERE klass_id = ?";
        List<Klass> klassById = jdbcTemplate.query(sqlKlass, CarStorageImpl::makeKlass, klassId);
        if (klassById.isEmpty() || klassById.get(0) == null || !klassById.get(0).getId().equals(klassId)) {
            throw new NotFoundException(String.format("Не найден Klass с id: %s", klassId));
        }
        Klass klass = klassById.get(0);
        return klass;
    }

    @Override
    public List<Type> getTypes(int idCar) {
        String sqlTypes =
            "SELECT g.type_id AS typeid, g.type_name AS typename FROM car_types fg JOIN types g ON fg.type_id = g.type_id WHERE fg.car_id = ?";
        return jdbcTemplate.query(sqlTypes, CarStorageImpl::makeType, idCar);
    }

    @Override
    public List<Type> checkType(Car car) {
        List<Type> carTypes = new ArrayList<>();
        if (car.getTypes() != null) {
            Set<Integer> typeSet =
                car.getTypes().stream().map(Type::getId).collect(Collectors.toSet());
            for (Integer id : typeSet) {
                String sqlTypesId =
                    "SELECT type_id AS typeid, type_name AS typename FROM types WHERE type_id = ?";
                List<Type> typesById = jdbcTemplate.query(sqlTypesId, CarStorageImpl::makeType, id);
                if (typesById.isEmpty()
                    || typesById.get(0) == null
                    || !typesById.get(0).getId().equals(id)) {
                    throw new NotFoundException(String.format("Не найден Type с id: %s", id));
                }
                carTypes.add(typesById.get(0));
            }
        }
        return carTypes;
    }

    @Override
    public void insertCarTypes(Car car) {
        String sqlInsertCarType = "INSERT INTO car_types (car_id, type_id) VALUES (?, ?)";
        if (car.getTypes() != null) {
            for (Type type : car.getTypes()) {
                jdbcTemplate.update(sqlInsertCarType, car.getId(), type.getId());
            }
        }
    }

    @Override
    public void deleteCarTypes(Car car) {
        String sqlDeleteCarTypes = "DELETE FROM car_types WHERE car_id = ?";
        jdbcTemplate.update(sqlDeleteCarTypes, car.getId());
    }


    @Override
    public void addDirectorsByCarId(int carId, int directorId) {
        String sqlInsert = "INSERT INTO cars_directors (car_id, director_id) VALUES (?, ?)";
        String sqlDelete = "DELETE FROM cars_directors WHERE car_id = ?";
        jdbcTemplate.update(sqlDelete, carId);
        jdbcTemplate.update(sqlInsert, carId, directorId);
    }

    @Override
    public Collection<Director> findDirectorsByCarId(Integer carId) {
        String sqlSelect =
            "SELECT d.* FROM cars_directors fd JOIN directors d ON fd.director_id = d.id WHERE fd.car_id = ?";
        return jdbcTemplate.query(sqlSelect, directorMapper, carId);
    }


    @Override
    public Collection<Car> findAllCars() {
        String sql = "SELECT f.*, " +
            "       group_concat(DISTINCT m.klass_name) AS klass, " +
            "       group_concat(DISTINCT CONCAT_WS(',', g.type_id, g.type_name) ORDER BY g.type_id SEPARATOR ';') AS types, " +
            "       COUNT(DISTINCT l.user_id) AS likes_count, " +
            "       group_concat(DISTINCT CONCAT_WS(',', d.id, d.name) ORDER BY d.name SEPARATOR ';') AS directors " +
            "FROM cars f " +
            "         LEFT JOIN cars_directors fd ON fd.car_id = f.car_id " +
            "         LEFT JOIN directors d ON d.id = fd.director_id " +
            "         INNER JOIN klass m ON m.klass_id = f.klass_id " +
            "         LEFT JOIN car_types fg ON fg.car_id = f.car_id " +
            "         LEFT JOIN types g ON g.type_id = fg.type_id " +
            "         LEFT JOIN likes l ON l.car_id = f.car_id " +
            "GROUP BY f.car_id";

        return jdbcTemplate.query(sql, carRowMapper);
    }

    @Override
    public void deleteDirectorsByCarId(Integer carId) {
        String sql = "DELETE FROM cars_directors WHERE car_id = ?";
        jdbcTemplate.update(sql, carId);
    }

    public static Klass makeKlass(ResultSet rs, int rowNum) throws SQLException {
        return Klass.builder()
            .id(rs.getInt("klass_id"))
            .name(rs.getString("klass_name"))
            .build();
    }

    public Collection<Car> getLikesCars(int userId) {

        String sqlSelect = "SELECT " +
            "f.car_id AS carid, " +
            "f.name AS name, " +
            "f.description AS description, " +
            "f.rate AS rate, f.releasedate AS releasedate, " +
            "f.price AS price,  " +
            "m.klass_id AS klassid, " +
            "m.klass_name AS klassname, " +
            "g.type_id AS typeid, " +
            "g.type_name AS typename " +
            "FROM cars AS f " +
            "INNER JOIN klass m ON m.klass_id = f.klass_id " +
            "LEFT OUTER JOIN cars_directors d ON f.car_id = d.car_id " +
            "JOIN likes l1 ON l1.car_id = f.car_id AND l1.user_id = ? " +
            "LEFT OUTER JOIN car_types fg ON fg.car_id = f.car_id " +
            "LEFT OUTER JOIN types g ON g.type_id = fg.type_id " +
            "ORDER BY f.rate DESC";
        return jdbcTemplate.query(sqlSelect, this::makeCar, userId);
    }

    @Override
    public Collection<Car> getPopularCarsByTypeAndYear(int count, Integer typeId, Integer year) {
        String sql = "SELECT " +
            "f.CAR_ID as carId, " +
            "f.NAME as name, " +
            "f.DESCRIPTION as description, " +
            "f.RATE as rate, " +
            "f.RELEASEDATE as releaseDate, " +
            "f.PRICE as price, " +
            "m.KLASS_ID AS klassId, " +
            "m.KLASS_NAME AS klassName, " +
            "g.TYPE_ID as typeId, " +
            "g.TYPE_NAME AS typeName " +
            "FROM " +
            "CARS f " +
            "LEFT JOIN KLASS m ON m.KLASS_ID = f.KLASS_ID " +
            "LEFT JOIN CAR_TYPES fg ON fg.CAR_ID = f.CAR_ID " +
            "LEFT JOIN TYPES g ON g.TYPE_ID = fg.TYPE_ID " +
            "WHERE true " +
            (typeId != null ? String.format("AND g.TYPE_ID = %d ", typeId) : "") +
            (year != null ? String.format("AND EXTRACT(YEAR FROM f.RELEASEDATE) = %d ", year) : "") +
            "GROUP BY f.CAR_ID " +
            "ORDER BY f.RATE DESC";
        return jdbcTemplate.query(sql, this::makeCar);
    }

    @Override
    public Collection<Car> getSearchCarsByDirector(String query) {
        String sql = "SELECT " +
            "f.CAR_ID as carId, " +
            "f.NAME as name, " +
            "f.DESCRIPTION as description, " +
            "f.RATE as rate, " +
            "f.RELEASEDATE as releaseDate, " +
            "f.PRICE as price, " +
            "m.KLASS_ID AS klassId, " +
            "m.KLASS_NAME AS klassName, " +
            "g.TYPE_ID as typeId, " +
            "g.TYPE_NAME AS typeName " +
            "FROM " +
            "directors d " +
            "JOIN cars_directors fd ON fd.director_id = d.id " +
            "JOIN cars f ON f.car_id = fd.car_id " +
            "JOIN KLASS m ON f.KLASS_ID = m.KLASS_ID " +
            "LEFT JOIN CAR_TYPES fg ON fg.CAR_ID = f.CAR_ID " +
            "LEFT JOIN TYPES g ON g.TYPE_ID = fg.TYPE_ID " +
            "WHERE d.name ILIKE '%" + query + "%'";

        return jdbcTemplate.query(sql, this::makeCar);
    }

    @Override
    public Collection<Car> getSearchCarsByTitle(String query) {
        String sql = "SELECT " +
            "f.CAR_ID as carId, " +
            "f.NAME as name, " +
            "f.DESCRIPTION as description, " +
            "f.RATE as rate, " +
            "f.RELEASEDATE as releaseDate, " +
            "f.PRICE as price, " +
            "m.KLASS_ID AS klassId, " +
            "m.KLASS_NAME AS klassName, " +
            "g.TYPE_ID as typeId, " +
            "g.TYPE_NAME AS typeName " +
            "FROM " +
            "CARS f " +
            "LEFT JOIN KLASS m ON m.KLASS_ID = f.KLASS_ID " +
            "LEFT JOIN CAR_TYPES fg ON fg.CAR_ID = f.CAR_ID " +
            "LEFT JOIN TYPES g ON g.TYPE_ID = fg.TYPE_ID " +
            "WHERE f.name ILIKE '%" + query + "%'";

        return jdbcTemplate.query(sql, this::makeCar);
    }

    public static Type makeType(ResultSet rs, int rowNum) throws SQLException {
        return Type.builder()
            .id(rs.getInt("typeId"))
            .name(rs.getString("typeName"))
            .build();
    }

    public Collection<Car> makeCar(ResultSet rs) throws SQLException {
        Map<Integer, Car> carsMap = new LinkedHashMap<>();
        while (rs.next()) {
            int carId = rs.getInt("carId");
            Car car = carsMap.get(carId);
            if (car == null) {
                Klass rating = Klass.builder()
                    .id(rs.getInt("klassId"))
                    .name(rs.getString("klassName"))
                    .build();
                car = Car.builder()
                    .id(carId)
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("releaseDate").toLocalDate())
                    .price(rs.getInt("price"))
                    .rate(rs.getInt("rate"))
                    .klass(rating)
                    .types(new ArrayList<>())
                    .directors(directorStorage.getDirectorsByCarId(carId))
                    .build();
                carsMap.put(carId, car);
            }
            String typeName = rs.getString("typeName");
            if (typeName != null && !typeName.isEmpty()) {
                Type type = Type.builder()
                    .id(rs.getInt("typeId"))
                    .name(typeName)
                    .build();
                car.getTypes().add(type);
            }
        }
        return carsMap.values();
    }
}
