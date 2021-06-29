package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.simbirsoft.practice.bookreviewsite.dto.BookDTO;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import com.simbirsoft.practice.bookreviewsite.repository.BookRepository;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Roman Leontev
 * 03:32 23.06.2021
 * group 11-905
 */

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<BookDTO> findAllByBookStatus(Pageable pageable, BookStatus bookStatus) {
        return bookRepository.findAllByBookStatus(pageable, bookStatus).map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public Page<BookDTO> findAllByBookStatusAndTitle(Pageable pageable, BookStatus bookStatus, String title) {
        return bookRepository.findAllByBookStatusAndTitleContainingIgnoreCase(pageable, bookStatus, title).map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public Page<BookDTO> getAllByBookStatusAndSortByReviews(Pageable pageable, BookStatus bookStatus) {
        return bookRepository.findAllByBookStatusWithSortByReview(pageable, bookStatus).map(book -> modelMapper.map(book, BookDTO.class));
    }
}
