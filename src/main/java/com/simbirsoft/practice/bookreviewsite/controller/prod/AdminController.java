package com.simbirsoft.practice.bookreviewsite.controller.prod;

import com.simbirsoft.practice.bookreviewsite.dto.AdminRejectForm;
import com.simbirsoft.practice.bookreviewsite.dto.BookDTO;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

/**
 * @author Anvar Khasanov
 * student of ITIS KFU
 * group 11-905
 */

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookService bookService;

    public AdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/moderation")
    public String getBooksToModeration(Pageable pageable, Model model) {
        Page<BookDTO> books = bookService.getAllByBookStatus(pageable, BookStatus.MODERATION);

        model.addAttribute("books", books);

        return "moderation";
    }

    @ResponseBody
    @GetMapping("/rest/moderation")
    public ResponseEntity<Page<BookDTO>> getRestBooksToModeration(
            @PageableDefault(size = 7, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<BookDTO> books = bookService.getAllByBookStatus(pageable, BookStatus.MODERATION);

        return ResponseEntity.ok(books);
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/moderation/accept/{id}")
    public String acceptBook(@PathVariable Long id) {
        bookService.acceptBookModeration(id);

        return "redirect:/moderation";
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/moderation/reject/{id}")
    public String rejectBook(@PathVariable Long id, AdminRejectForm adminRejectForm) {
        bookService.rejectBookModeration(id, adminRejectForm.getResponse());

        return "redirect:/moderation";
    }
}
