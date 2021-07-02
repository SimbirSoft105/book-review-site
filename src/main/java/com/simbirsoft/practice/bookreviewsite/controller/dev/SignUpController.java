package com.simbirsoft.practice.bookreviewsite.controller.dev;

import com.simbirsoft.practice.bookreviewsite.enums.Role;
import com.simbirsoft.practice.bookreviewsite.dto.SignUpForm;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;
import com.simbirsoft.practice.bookreviewsite.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("signUp")
@Profile("dev")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @GetMapping
    public String getSignUpPage(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "signUp";
    }

    @PostMapping
    public String signUp(@Valid SignUpForm signUpForm,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {

            if (bindingResult.hasGlobalErrors()) {
                ObjectError passwordMismatchError = bindingResult.getGlobalError();
                model.addAttribute("passwordMismatch", passwordMismatchError.getDefaultMessage());
            }
            model.addAttribute("signUpForm", signUpForm);
            return "signUp";
        }
        else {
            signUpService.devSignUpWithRole(signUpForm, Role.ROLE_USER);
        } return "redirect:/login";
    }

    @GetMapping("profileEditConfirmEmail")
    public String confirmChangedEmail() {
        return "email_changed_confirm_email";
    }

    @GetMapping("confirm_email/{confirm_code}")
    public String confirmEmail(@PathVariable("confirm_code") String confirmCode)
            throws UserNotFoundException {

        signUpService.confirmUserByConfirmCode(confirmCode);
        return "email_confirmed";
    }

    @GetMapping("registration_confirm_email")
    public String getEmailConfirmationPage() {
        return "confirm_email";
    }
}
