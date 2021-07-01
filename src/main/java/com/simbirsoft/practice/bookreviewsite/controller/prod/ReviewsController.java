package com.simbirsoft.practice.bookreviewsite.controller.prod;

import com.simbirsoft.practice.bookreviewsite.dto.ReviewDTO;
import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.security.details.CustomUserDetails;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import com.simbirsoft.practice.bookreviewsite.service.ReviewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("my")
    public String getReviewsPage(Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 @PageableDefault(size = 5,
                                         sort = "createdAt",
                                         direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ReviewDTO> reviews = reviewsService.getAllByUserId(
                userDetails.getUser().getId(),
                pageable);
        model.addAttribute("reviews", reviews);

        return "myReviews";
    }

    @GetMapping("my/ajax")
    public ResponseEntity<Page<ReviewDTO>> loadReviews(Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 @PageableDefault(size = 5,
                                         sort = "createdAt",
                                         direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ReviewDTO> reviews = reviewsService.getAllByUserId(
                userDetails.getUser().getId(), pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("{reviewId}/delete")
    public String deleteReviewAndReloadPage(@PathVariable("reviewId") Long reviewId) {
        reviewsService.deleteById(reviewId);
        return "redirect:/reviews/my";
    }

}
