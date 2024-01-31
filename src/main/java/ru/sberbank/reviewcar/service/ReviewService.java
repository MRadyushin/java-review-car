package ru.sberbank.reviewcar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.dao.eventEnum.EventOperation;
import ru.sberbank.reviewcar.dao.eventEnum.EventType;
import ru.sberbank.reviewcar.dao.ReviewStorage;
import ru.sberbank.reviewcar.model.Review;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewStorage reviewStorageDb;
    private final UserService userService;
    private final CarService carService;
    private final EventService eventService;

     public Review addReview(Review review) {
        userService.getUser(review.getUserId());
        carService.getCar(review.getCarId());
        Review r = reviewStorageDb.addReview(review);
        eventService.createEvent(r.getUserId(), EventType.REVIEW, EventOperation.ADD, r.getReviewId());
        return r;
    }

   public Review updateReview(Review review) {
        userService.getUser(review.getUserId());
        carService.getCar(review.getCarId());
        Review r = reviewStorageDb.updateReview(review);
        eventService.createEvent(r.getUserId(), EventType.REVIEW, EventOperation.UPDATE, r.getReviewId());
        return r;
    }

    public void deleteReview(int reviewId) {
        Review review = reviewStorageDb.getReview(reviewId);
        eventService.createEvent(review.getUserId(), EventType.REVIEW, EventOperation.REMOVE, review.getReviewId());
        reviewStorageDb.deleteReview(reviewId);
    }

    public Review getReview(int reviewId) {
        Review review = reviewStorageDb.getReview(reviewId);
        if (review == null) {
            throw new NotFoundException("Отзыв с id=" + reviewId + " не найден");
        }
        return review;
    }

    public Collection<Review> getAllReviews(Integer carId, Integer count) {
        return reviewStorageDb.getAllReviews(carId, count);
    }

    public void makeLikeReview(int reviewId, int userId) {
        reviewStorageDb.makeLikeOrDislike(reviewId, userId, true);
    }

    public void makeDislikeReview(int reviewId, int userId) {
        reviewStorageDb.makeLikeOrDislike(reviewId, userId, false);
    }

    public void deleteLikeReview(int reviewId, int userId) {
        reviewStorageDb.deleteLikeOrDislike(reviewId, userId, true);
    }

    public void deleteDislikeReview(int reviewId, int userId) {
        reviewStorageDb.deleteLikeOrDislike(reviewId, userId, false);
    }
}
