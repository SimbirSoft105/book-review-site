package com.simbirsoft.practice.bookreviewsite.controller.prod;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("login")
public class LoginController {

    @GetMapping
    public String getLoginPage(@RequestParam(value = "error", required = false) String error,
                               Model model) {
        if (error !=  null) {
            model.addAttribute("error", "Неверный email или пароль");
        }
        return "auth";
    }

}
