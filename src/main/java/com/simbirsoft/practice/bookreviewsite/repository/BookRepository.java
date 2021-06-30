package com.simbirsoft.practice.bookreviewsite.repository;

import com.simbirsoft.practice.bookreviewsite.entity.Book;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;

import java.util.Optional;

/**
 * @author Roman Leontev
 * 03:33 23.06.2021
 * group 11-905
 */

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByBookStatus(Pageable pageable, BookStatus bookStatus);

    Page<Book> findAllByBookStatusAndTitleContainingIgnoreCase(Pageable pageable, BookStatus bookStatus, String title);

    int countBookByPushedById(Long id);

    Page<Book> findAllByPushedById(Pageable pageable, Long userId);

    Optional<Book> findFirstByBookStatusOrderById(BookStatus bookStatus);

    Page<Book> findAllByLikedUsersIsContaining(Pageable pageable, User user);

    @Transactional
    @Query("update Book book set book.rate = :rate where book.id = :id")
    public void recalculateBookRate(@RequestParam("rate") float rate,
                                    @RequestParam("id") Long id);

}
