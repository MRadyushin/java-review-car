package ru.sberbank.reviewcar.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.sberbank.reviewcar.model.Review;
import ru.sberbank.reviewcar.dao.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@RequiredArgsConstructor
@Component
@Primary
@Slf4j
public class ReviewStorageImpl implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review addReview(Review review) {
        String sqlInsertReview = "INSERT INTO reviews (content, is_positive, user_id, car_id) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlInsertReview, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, review.getContent());
            ps.setBoolean(2, review.getIsPositive());
            ps.setInt(3, review.getUserId());
            ps.setInt(4, review.getCarId());
            return ps;
        }, keyHolder);
        int reviewId = keyHolder.getKey().intValue();
        review.setReviewId(reviewId);
        log.info("Отзыв c id=" + reviewId + " добавлен");
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sqlUpdateReview = "UPDATE reviews SET content = ?, is_positive = ? WHERE review_id = ?";
        jdbcTemplate.update(sqlUpdateReview,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        log.info("Отзыв c id=" + review.getReviewId() + " обновлён");
        return getReview(review.getReviewId());
    }

    @Override
    public void deleteReview(int reviewId) {
        String sqlDelete = "DELETE FROM reviews WHERE review_id = ?";
        jdbcTemplate.update(sqlDelete, reviewId);
        log.info("Отзыв c id=" + reviewId + " удалён");
    }

    @Override
    public Review getReview(int reviewId) {
        String sqlSelect = "SELECT * FROM reviews WHERE review_id = ?";
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(sqlSelect, reviewId);
        Review review = null;
        if (reviewRows.next()) {
            review = Review.builder()
                    .reviewId(reviewId)
                    .content(reviewRows.getString("content"))
                    .isPositive(reviewRows.getBoolean("is_positive"))
                    .useful(calculateReviewUseful(reviewId))
                    .userId(reviewRows.getInt("user_id"))
                    .carId(reviewRows.getInt("car_id")).build();
        }
        return review;
    }

    @Override
    public Collection<Review> getAllReviews(Integer carId, Integer count) {
        StringBuilder sqlQuery = new StringBuilder("SELECT DISTINCT ON (r.review_id) r.review_id, r.content, r.is_positive, r.user_id, r.car_id, ru.is_positive " +
                "FROM reviews r LEFT JOIN review_useful ru ON r.review_id = ru.review_id");

        if (carId != null) {
            sqlQuery.append(" WHERE car_id = ?");
        }

        sqlQuery.append(" GROUP BY r.review_id, r.content, r.is_positive, r.user_id, r.car_id, ru.is_positive ");
        sqlQuery.append(" ORDER BY COUNT(CASE WHEN ru.is_positive = true THEN 1 ELSE NULL END) DESC," +
                " COUNT(CASE WHEN ru.is_positive = false THEN 1 ELSE NULL END) NULLS LAST");

        if (count != null && count > 0) {
            sqlQuery.append(" LIMIT ? ");
        }

         return jdbcTemplate.query(sqlQuery.toString(), ps -> {
            int parameterIndex = 1;
            if (carId != null) {
                ps.setInt(parameterIndex++, carId);
            }
            if (count != null && count > 0) {
                ps.setInt(parameterIndex, count);
            }
        }, (rs, rowNum) -> {
            Review review = Review.builder()
                    .reviewId(rs.getInt("review_id"))
                    .content(rs.getString("content"))
                    .isPositive(rs.getBoolean("is_positive"))
                    .useful(calculateReviewUseful(rs.getInt("review_id")))
                    .userId(rs.getInt("user_id"))
                    .carId(rs.getInt("car_id"))
                    .build();

            return review;
         });
    }

    @Override
    public void makeLikeOrDislike(int reviewId, int userId, boolean grade) {
        String sqlInsert = "INSERT INTO review_useful (review_id, is_positive, user_id) VALUES (?, ?, ?)";
        if (grade == true) {
            jdbcTemplate.update(sqlInsert, reviewId, true, userId);
            log.info("Пользователь с id=" + userId + " поставил лайк отзыву с id=" + reviewId);
        } else {
            jdbcTemplate.update(sqlInsert, reviewId, false, userId);
            log.info("Пользователь с id=" + userId + " поставил дизлайк отзыву с id=" + reviewId);
        }
    }

    @Override
    public void deleteLikeOrDislike(int reviewId, int userId, boolean grade) {
        String sqlDelete = "DELETE FROM review_useful WHERE user_id = ? AND review_id = ? AND is_positive = ?";
        if (grade == true) {
            jdbcTemplate.update(sqlDelete, userId, reviewId, true);
            log.info("Пользователь с id=" + userId + " удалил лайк отзыву с id=" + reviewId);
        } else {
            jdbcTemplate.update(sqlDelete, userId, reviewId, false);
            log.info("Пользователь с id=" + userId + " удалил дизлайк отзыву с id=" + reviewId);
        }
    }

    public Integer calculateReviewUseful(int reviewId) {
        Integer rating;
        String sql = "SELECT COUNT(*) FROM review_useful WHERE review_id = ? AND is_positive = ?";
        Integer likes = jdbcTemplate.queryForObject(sql, new Object[]{reviewId, true}, Integer.class);
        Integer dislikes = jdbcTemplate.queryForObject(sql, new Object[]{reviewId, false}, Integer.class);
        if (likes != null & dislikes != null) {
            rating = likes - dislikes;
        } else if (likes == null && dislikes != null) {
            rating = -dislikes;
        } else if (likes != null && dislikes == null) {
            rating = likes;
        } else {
            rating = 0;
        }
        return rating;
    }
}
