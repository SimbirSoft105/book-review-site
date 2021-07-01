package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.simbirsoft.practice.bookreviewsite.dto.*;
import com.simbirsoft.practice.bookreviewsite.entity.Book;
import com.simbirsoft.practice.bookreviewsite.entity.Review;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.exception.ResourceNotFoundException;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;
import com.simbirsoft.practice.bookreviewsite.repository.BookRepository;
import com.simbirsoft.practice.bookreviewsite.repository.ReviewsRepository;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import com.simbirsoft.practice.bookreviewsite.service.ReviewsService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewsServiceImpl implements ReviewsService {

    private final ReviewsRepository reviewsRepository;

    private final BookRepository bookRepository;

    private final UsersRepository usersRepository;

    private final BookService bookService;

    private final ModelMapper modelMapper;

    public ReviewsServiceImpl(ReviewsRepository reviewsRepository, ModelMapper modelMapper,
                              BookRepository bookRepository, UsersRepository usersRepository,
                              BookService bookService) {

        this.reviewsRepository = reviewsRepository;
        this.modelMapper = modelMapper;
        this.bookRepository = bookRepository;
        this.usersRepository = usersRepository;
        this.bookService = bookService;
    }

    @Override
    public int getReviewsCountUserWrote(Long userId) {
        return reviewsRepository.countReviewsByAuthorId(userId);
    }

    @Override
    public Page<ReviewDTO> getAllByBookId(Long bookId, Pageable pageable) {
        return reviewsRepository.getAllByBookId(bookId, pageable)
                .map(review -> modelMapper.map(review, ReviewDTO.class));
    }

    @Override
    public ReviewAdditionReturnDTO addReview(ReviewAdditionDTO reviewAdditionDTO, Long authorId, Long bookId) {

        Book book = bookRepository.findById(bookId).orElseThrow(() ->
                new ResourceNotFoundException("Book not found"));

        User author = usersRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Review review = Review.builder()
                .book(book)
                .author(author)
                .mark(Integer.parseInt(reviewAdditionDTO.getMark()))
                .text(reviewAdditionDTO.getText())
                .createdAt(LocalDateTime.now())
                .rate(0)
                    .build();
        reviewsRepository.save(review);

        return ReviewAdditionReturnDTO.builder()
                .id(review.getId())
                .createdAt(review.getCreatedAt())
                .rate(bookService.recalculateBookRate(bookId))
                    .build();

    }

    @Override
    public Long deleteById(Long id) {
        Review review = reviewsRepository.getById(id);
        reviewsRepository.delete(review);
        return review.getBook().getId();
    }

    @Override
    public Page<ReviewDTO> getAllByUserId(Long userId, Pageable pageable) {
        return reviewsRepository.getAllByAuthorId(userId, pageable)
                .map(review -> modelMapper.map(review, ReviewDTO.class));
    }

}
