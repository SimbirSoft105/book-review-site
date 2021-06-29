package com.simbirsoft.practice.bookreviewsite.controller.prod;

import com.simbirsoft.practice.bookreviewsite.dto.BookDTO;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Roman Leontev
 * 03:04 23.06.2021
 * group 11-905
 */

@Controller
public class AllBooksController {

    private final BookService bookService;

    @Autowired
    public AllBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/allBooks")
    public String getAllBooksPage(@PageableDefault(size = 7, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<BookDTO> books = bookService.getAllByBookStatus(pageable, BookStatus.PUBLIC);

        model.addAttribute("title", "All Films");
        model.addAttribute("books", books);
        return "allBooks";
    }

    @GetMapping("/rest/allBooks")
    @ResponseBody
<<<<<<< Updated upstream
    public ResponseEntity<Page<BookDTO>> getAllBooks(@PageableDefault(size = 7, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable, String title) {
=======
    @GetMapping("/rest/all")
    public ResponseEntity<Page<BookDTO>> getAllBooks(String title, Boolean sortByReviews,
            @PageableDefault(size = 7, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {

>>>>>>> Stashed changes
        Page<BookDTO> books;
        if (title == null && (sortByReviews == null || !sortByReviews)) {
            books = bookService.getAllByBookStatus(pageable, BookStatus.PUBLIC);
        } else if (sortByReviews == null || !sortByReviews) {
            books = bookService.getAllByBookStatusAndTitle(pageable, BookStatus.PUBLIC, title);
        } else {
            books = bookService.getAllByBookStatusAndSortByReviews(pageable, BookStatus.PUBLIC);
        }

        if (books == null || books.getContent().size() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(books);
    }
}
