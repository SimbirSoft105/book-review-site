package com.simbirsoft.practice.bookreviewsite.controller.prod;

import com.simbirsoft.practice.bookreviewsite.dto.*;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import com.simbirsoft.practice.bookreviewsite.security.details.CustomUserDetails;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import com.simbirsoft.practice.bookreviewsite.service.CountryService;
import com.simbirsoft.practice.bookreviewsite.service.LanguageService;
import com.simbirsoft.practice.bookreviewsite.service.ReviewsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * @author Roman Leontev
 * 03:04 23.06.2021
 * group 11-905
 */

@Controller
@RequestMapping("/book")
public class BooksController {

    private final BookService bookService;

    private final LanguageService languageService;

    private final CountryService countryService;

    private final ReviewsService reviewsService;

    private final ModelMapper modelMapper;

    @Autowired
    public BooksController(BookService bookService, LanguageService languageService,
                           CountryService countryService, ReviewsService reviewsService,
                           ModelMapper modelMapper) {

        this.bookService = bookService;
        this.languageService = languageService;
        this.countryService = countryService;
        this.reviewsService = reviewsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public String getAllBooksPage(@PageableDefault(size = 7, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<BookDTO> books = bookService.getAllByBookStatus(pageable, BookStatus.PUBLIC);

        model.addAttribute("books", books);

        return "all_books";
    }

    @ResponseBody
    @GetMapping("/rest/all")
    public ResponseEntity<Page<BookDTO>> getAllBooks(String title, Boolean sortByReviews,
            @PageableDefault(size = 7, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<BookDTO> books;
        if (title == null && (sortByReviews == null || !sortByReviews)) {
            books = bookService.getAllByBookStatus(pageable, BookStatus.PUBLIC);
        } else if (sortByReviews == null || !sortByReviews) {
            books = bookService.getAllByBookStatusAndTitle(pageable, BookStatus.PUBLIC, title);
        } else {
            books = bookService.getAllByBookStatusAndSortByReviews(pageable, BookStatus.PUBLIC);
        }

        return ResponseEntity.ok(books);
    }

    @GetMapping("/add")
    public String getPage(AddBookForm addBookForm, Model model) {

        model.addAttribute("categories", bookService.getAllBookCategory());
        model.addAttribute("languages", languageService.getAllLanguages());
        model.addAttribute("countries", countryService.getAllCountries());
        model.addAttribute("addBookForm", addBookForm);

        return "add_book";
    }

    @PostMapping("/add")
    public String addBook(@Valid AddBookForm addBookForm, BindingResult bindingResult,
                          @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        if (bindingResult.hasErrors()) {
            return getPage(addBookForm, model);
        }

        bookService.createNewBook(addBookForm, userDetails.getUser().getId());

        return "redirect:/book/my";
    }

    @GetMapping("{bookId}")
    public String getBookPage(@PathVariable("bookId") Long bookId,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model,
                              @PageableDefault(size = 5,
                                      sort = "createdAt",
                                      direction = Sort.Direction.DESC) Pageable pageable) {

        BookDTO book = bookService.getById(bookId);
        Page<ReviewDTO> reviews = reviewsService.getAllByBookId(bookId, pageable);

        model.addAttribute("book", book);
        model.addAttribute("reviews", reviews);

        return "singleBook";
    }

    @GetMapping("{bookId}/ajax")
    public ResponseEntity<Page<ReviewDTO>> getBookPage(@PathVariable("bookId") Long bookId,
                                                       @PageableDefault(size = 5,
                                                               sort = "createdAt",
                                                               direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ReviewDTO> reviews = reviewsService.getAllByBookId(bookId, pageable);
        return ResponseEntity.ok(reviews);
    }


    @PostMapping("{bookId}/addReview")
    public ResponseEntity<ReviewAdditionReturnDTO> addReview(
            @RequestBody ReviewAdditionDTO reviewAdditionDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("bookId") Long bookId) {

        return ResponseEntity.ok(reviewsService.addReview(reviewAdditionDTO,
                userDetails.getUser(), bookId));
    }
}
