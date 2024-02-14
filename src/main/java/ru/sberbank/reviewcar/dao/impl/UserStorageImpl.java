package ru.sberbank.reviewcar.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.sberbank.reviewcar.dao.UserStorage;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Component
@Primary
@Slf4j
public class UserStorageImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     *
     * @param id - id пользователя
     * @return возвращается пользователь по id
     */
    @Override
    public User getUser(int id) {
        User user;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", id);
        user = null;
        if (userRows.next()) {
            user = User.builder().id(id)
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate()).build();
        }
        return user;
    }

    /**
     *
     * @return возвращаются все пользователи
     */
    @Override
    public Collection<User> getAllUsers() {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users");
        Collection<User> users = new ArrayList<>();
        while (userRows.next()) {
            User user = User.builder().id(userRows.getInt("user_id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate()).build();
            users.add(user);
        }
        return users;
    }

    /**
     *
     * создание пользователя
     */
    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(id);
        return user;
    }

    /**
     *
     * изменение пользователя
     */
    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email=?, login=?, name=?, birthday=? WHERE user_id=?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    /**
     *
     * удаление пользователя
     */
    @Override
    public String deleteUser(int userId) {
        try {
            String sqlQuery = "DELETE FROM USERS WHERE USER_ID =?";
            jdbcTemplate.update(sqlQuery, userId);
            log.info("Пользователь " + userId + " удалён.");
        } catch (DataAccessException e) {
            log.debug("Пользователь " + userId + " не удалён/ не найден в Базе.");
            throw new NotFoundException("пользователь с id " + userId + " не может быть удалён");
        }
        return "Пользователь " + userId + " удалён.";
    }

    /**
     *
     * @param userId - id пользователя
     * @param targetUser - пользователь
     * @return возвращает рекомендованные авто
     */
    @Override
    public List<Integer> getRecommendations(int userId, User targetUser) {
        Map<User, List<Integer>> usersWithLikes = new HashMap<>();

        String sql = "SELECT u2.*, COUNT(l2.car_id) as likes_intersection " +
                "FROM likes l1 " +
                "JOIN likes l2 ON l1.car_id = l2.car_id AND l1.user_id != l2.user_id " +
                "JOIN users u2 on u2.user_id = l2.user_id " +
                "WHERE l1.user_id = ? " +
                "GROUP BY l1.user_id, l2.user_id, u2.user_id " +
                "ORDER BY likes_intersection DESC " +
                "LIMIT 10";

        String userLikesSql = "Select car_id from likes where user_id = ?";
        SqlRowSet userLikesRs = jdbcTemplate.queryForRowSet(sql, userId);
        while (userLikesRs.next()) {
            User user = User.builder().id(userLikesRs.getInt("user_id"))
                    .email(userLikesRs.getString("email"))
                    .login(userLikesRs.getString("login"))
                    .name(userLikesRs.getString("name"))
                    .birthday(Objects.requireNonNull(userLikesRs.getDate("birthday")).toLocalDate()).build();
            usersWithLikes.put(user, new ArrayList<>());
        }
        usersWithLikes.keySet().forEach(user -> usersWithLikes.get(user).addAll(jdbcTemplate.queryForList(userLikesSql,
                Integer.class, user.getId())));
        if (!usersWithLikes.containsKey(targetUser)) {
            List<Integer> targetUserLikes = jdbcTemplate.queryForList(userLikesSql, Integer.class, userId);
            usersWithLikes.put(targetUser, targetUserLikes);
        }

        List<Integer> recommendations = new ArrayList<>();
        List<User> similarUsers = new ArrayList<>(usersWithLikes.keySet());
        for (User similaruser : similarUsers) {
            if (similaruser.equals(targetUser)) {
                continue;
            }
            List<Integer> similarUserLikes = usersWithLikes.get(similaruser);
            similarUserLikes.removeAll(usersWithLikes.get(targetUser));
            recommendations.addAll(similarUserLikes);
        }

        List<Integer> recommendedCarsIds = new ArrayList<>();
        for (int carId : recommendations) {
            if (!usersWithLikes.get(targetUser).contains(carId)) {
                recommendedCarsIds.add(carId);
                log.info("Пользователю рекомендована машина с id " + carId);
            }
        }
        return recommendedCarsIds;
    }
}
