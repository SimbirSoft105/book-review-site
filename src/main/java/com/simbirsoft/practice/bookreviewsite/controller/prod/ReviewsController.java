package com.simbirsoft.practice.bookreviewsite.controller.prod;

import com.simbirsoft.practice.bookreviewsite.service.BookService;
import com.simbirsoft.practice.bookreviewsite.service.ReviewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;

    private final BookService bookService;

    public ReviewsController(ReviewsService reviewsService, BookService bookService) {
        this.reviewsService = reviewsService;
        this.bookService = bookService;
    }

    @DeleteMapping("{reviewId}/delete")
    public ResponseEntity<Float> deleteReview(@PathVariable("reviewId") Long reviewId) {
        Long bookId = reviewsService.deleteById(reviewId);
        float newBookRate = bookService.recalculateBookRate(bookId);
        return ResponseEntity.ok(newBookRate);
    }

}
