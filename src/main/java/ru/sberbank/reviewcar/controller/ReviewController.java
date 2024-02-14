package ru.sberbank.reviewcar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.sberbank.reviewcar.model.Review;
import ru.sberbank.reviewcar.service.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     *
     * @param reviewId - id отзыва
     * @return вывод отзыва по id
     */
    @GetMapping(value = "/{reviewId}")
    private Review getReview(@PathVariable int reviewId) {
        return reviewService.getReview(reviewId);
    }

    /**
     *
     * @param carId - id машины
     * @param count - количество отзывов
     * @return - вывод всех отзывов
     */
    @GetMapping
    private Collection<Review> getAllReviews(@RequestParam(required = false) Integer carId, @RequestParam(defaultValue = "10") Integer count) {
        return reviewService.getAllReviews(carId, count);
    }

    /**
     *
     * создане отзыва
     */
    @PostMapping
    private Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    /**
     *
     * изменение отзыва
     */
    @PutMapping
    private Review updateReview(@RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    /**
     * удаление отзыва
     */
    @DeleteMapping(value = "/{reviewId}")
    private void deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
    }

    /**
     * установка лайка на отзыв
     * @param reviewId - id отзыва
     * @param userId - id поользователя
     */
    @PutMapping(value = "/{reviewId}/like/{userId}")
    private void makeLikeReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.makeLikeReview(reviewId, userId);
    }
    /**
     * установка дизлайка на отзыв
     * @param reviewId - id отзыва
     * @param userId - id поользователя
     */
    @PutMapping(value = "/{reviewId}/dislike/{userId}")
    private void makeDislikeReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.makeDislikeReview(reviewId, userId);
    }
    /**
     * удаление лайка на отзыв
     * @param reviewId - id отзыва
     * @param userId - id поользователя
     */
    @DeleteMapping(value = "/{reviewId}/like/{userId}")
    private void deleteLikeReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.deleteLikeReview(reviewId, userId);
    }
    /**
     * удаление дизлайка на отзыв
     * @param reviewId - id отзыва
     * @param userId - id поользователя
     */
    @DeleteMapping(value = "/{reviewId}/dislike/{userId}")
    private void deleteDislikeReview(@PathVariable int reviewId, @PathVariable int userId) {
        reviewService.deleteDislikeReview(reviewId, userId);
    }
}