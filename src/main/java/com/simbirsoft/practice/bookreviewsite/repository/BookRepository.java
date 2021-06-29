package com.simbirsoft.practice.bookreviewsite.repository;

import com.simbirsoft.practice.bookreviewsite.dto.BookDTO;
import com.simbirsoft.practice.bookreviewsite.entity.Book;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Roman Leontev
 * 03:33 23.06.2021
 * group 11-905
 */

    public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByBookStatus(Pageable pageable, BookStatus bookStatus);

    Page<Book> findAllByBookStatusAndTitleContainingIgnoreCase(Pageable pageable, BookStatus bookStatus, String title);
<<<<<<< Updated upstream
=======

    int countBookByPushedById(Long id);

    Page<Book> findAllByPushedById(Pageable pageable, Long userId);

    Optional<Book> findFirstByBookStatusOrderById(BookStatus bookStatus);

    @Query("select b from Book b LEFT JOIN Review r on b.id = r.book.id and b.bookStatus = :bookStatus group by b.id order by count(r.id) desc")
    Page<Book> findAllByBookStatusWithSortByReview(Pageable pageable, BookStatus bookStatus);
>>>>>>> Stashed changes
}
