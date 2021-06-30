package com.simbirsoft.practice.bookreviewsite.service;

import com.simbirsoft.practice.bookreviewsite.dto.ReviewAdditionDTO;
import com.simbirsoft.practice.bookreviewsite.dto.ReviewAdditionReturnDTO;
import com.simbirsoft.practice.bookreviewsite.dto.ReviewDTO;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewsService {

    int getReviewsCountUserWrote(Long userId);

    Page<ReviewDTO> getAllByBookId(Long bookId, Pageable pageable);

    ReviewAdditionReturnDTO addReview(ReviewAdditionDTO reviewAdditionDTO, Long authorId, Long bookId);

    Long deleteById(Long id);

}
