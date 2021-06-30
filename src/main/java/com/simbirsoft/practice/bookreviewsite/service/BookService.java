package com.simbirsoft.practice.bookreviewsite.service;

import com.simbirsoft.practice.bookreviewsite.dto.AddBookForm;
import com.simbirsoft.practice.bookreviewsite.dto.BookDTO;
import com.simbirsoft.practice.bookreviewsite.dto.CategoryDTO;
import com.simbirsoft.practice.bookreviewsite.dto.ReviewAdditionDTO;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Roman Leontev
 * 03:31 23.06.2021
 * group 11-905
 */

public interface BookService {
    Page<BookDTO> findAllByBookStatus(Pageable pageable, BookStatus bookStatus);

    Page<BookDTO> findAllByBookStatusAndTitle(Pageable pageable, BookStatus bookStatus, String title);

    int getBooksCountUserPushed(Long userId);

    List<CategoryDTO> getAllBookCategory();

    BookDTO createNewBook(AddBookForm addBookForm, Long userId);

    Page<BookDTO> getAllUserBooks(Pageable pageable, Long userId);

    boolean deleteUserBook(Long bookId, Long userId);

    BookDTO getFirstByBookStatus(BookStatus bookStatus);

    Page<BookDTO> getTopByBookStatus(Pageable pageable, BookStatus bookStatus);

    BookDTO getById(Long id);

    Page<BookDTO> getUserFavoriteBooks(Pageable pageable, Long userId);

    boolean deleteUserFavoriteBook(Long bookId, Long userId);

//    float recalculateBookRate(ReviewAdditionDTO reviewAdditionDTO, Long bookId);
}
