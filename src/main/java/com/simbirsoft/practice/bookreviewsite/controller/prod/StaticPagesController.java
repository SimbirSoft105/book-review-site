package com.simbirsoft.practice.bookreviewsite.controller.prod;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Anvar Khasanov
 * student of ITIS KFU
 * group 11-905
 */

@Controller
public class StaticPagesController {

    @GetMapping("/login_or_sign_up")
    public String getLoginOrSignUpPage() {
        return "login";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @GetMapping("/book/added")
    public String bookAdded() {
        return "book_added";
    }
}
