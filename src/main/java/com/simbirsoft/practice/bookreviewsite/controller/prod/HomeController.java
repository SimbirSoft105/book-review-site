package com.simbirsoft.practice.bookreviewsite.controller.prod;

import com.simbirsoft.practice.bookreviewsite.dto.BookDTO;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Roman Leontev
 * 15:22 27.06.2021
 * group 11-905
 */

@Controller
public class HomeController {

    private final BookService bookService;

    @Autowired
    public HomeController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        BookDTO firstBook;
        try {
            firstBook = bookService.getFirstByBookStatus(BookStatus.PUBLIC);
        } catch (IllegalArgumentException e) {
            firstBook = null;
        }

        Pageable pageableForTopBooks = PageRequest.of(0, 4, Sort.Direction.DESC, "rate");
        Page<BookDTO> topBooks = bookService.getAllByBookStatus(pageableForTopBooks, BookStatus.PUBLIC);

        Pageable pageableForFreshBooks = PageRequest.of(0, 4, Sort.Direction.DESC, "releaseYear");
        Page<BookDTO> freshBooks = bookService.getAllByBookStatus(pageableForFreshBooks, BookStatus.PUBLIC);

        model.addAttribute("freshBooks", freshBooks);
        model.addAttribute("topBooks", topBooks);
        model.addAttribute("firstBook", firstBook);
        return "index";
    }
}
