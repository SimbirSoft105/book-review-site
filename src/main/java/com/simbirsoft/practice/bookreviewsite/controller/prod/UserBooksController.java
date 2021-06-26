package com.simbirsoft.practice.bookreviewsite.controller.prod;

import com.simbirsoft.practice.bookreviewsite.dto.BookDTO;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import com.simbirsoft.practice.bookreviewsite.security.details.CustomUserDetails;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author Anvar Khasanov
 * student of ITIS KFU
 * group 11-905
 */

@Controller
@RequestMapping("/book")
public class UserBooksController {

    private final BookService bookService;

    public UserBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/my")
    public String userBooks(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @PageableDefault(size = 7, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
                            Model model) {

        Page<BookDTO> bookDTOList = bookService.getAllUserBooks(pageable, userDetails.getUser().getId());
        model.addAttribute("books", bookDTOList);

        return "user_books";
    }

    @ResponseBody
    @GetMapping("/rest/user/{id}")
    public ResponseEntity<Page<BookDTO>> getUserBooks(@PathVariable Long id,
            @PageableDefault(size = 7, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<BookDTO> books = bookService.getAllUserBooks(pageable, id);

        if (books == null || books.getContent().size() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(books);
    }

    @PostMapping("/delete/{id}")
    public String deleteUserBook(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        bookService.deleteUserBook(id, userDetails.getUser().getId());

        return "redirect:/book/my";
    }
}
