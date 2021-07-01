package com.simbirsoft.practice.bookreviewsite.repository;

import com.simbirsoft.practice.bookreviewsite.entity.Review;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Review, Long> {

    int countReviewsByAuthorId(Long id);

    Page<Review> getAllByBookId(Long id, Pageable pageable);

    List<Review> getAllByBookId(Long id);

    Page<Review> getAllByAuthorId(Long authorId, Pageable pageable);

}
