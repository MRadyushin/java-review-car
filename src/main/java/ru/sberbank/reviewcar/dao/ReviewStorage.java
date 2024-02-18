package ru.sberbank.reviewcar.dao;

import ru.sberbank.reviewcar.model.Review;

import java.util.Collection;

public interface ReviewStorage {

    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReview(int reviewId);

    Review getReview(int reviewId);

    Collection<Review> getAllReviews(Integer carId, Integer count);

    void makeLikeOrDislike(int reviewId, int userId, boolean grade);

    void deleteLikeOrDislike(int reviewId, int userId, boolean grade);

}