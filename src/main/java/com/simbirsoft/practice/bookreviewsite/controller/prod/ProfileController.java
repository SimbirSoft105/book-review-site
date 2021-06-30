package com.simbirsoft.practice.bookreviewsite.controller.prod;

import com.simbirsoft.practice.bookreviewsite.dto.ProfileEditForm;
import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.enums.UserStatus;
import com.simbirsoft.practice.bookreviewsite.security.details.CustomUserDetails;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import com.simbirsoft.practice.bookreviewsite.service.ReviewsService;
import com.simbirsoft.practice.bookreviewsite.service.SignUpService;
import com.simbirsoft.practice.bookreviewsite.service.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("profile")
public class ProfileController {

    private final UsersService usersService;
    private final BookService bookService;
    private final ReviewsService reviewsService;
    private final ModelMapper modelMapper;
    private final SignUpService signUpService;

    public ProfileController(UsersService usersService, BookService bookService,
                             ReviewsService reviewsService, ModelMapper modelMapper, SignUpService signUpService) {
        this.usersService = usersService;
        this.bookService = bookService;
        this.reviewsService = reviewsService;
        this.modelMapper = modelMapper;
        this.signUpService = signUpService;
    }

    @GetMapping
    public String getProfilePage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {

        model.addAttribute("title", "Профиль");

        Long userId = userDetails.getUser().getId();
        model.addAttribute("booksPushedCount", bookService.getBooksCountUserPushed(userId));
        model.addAttribute("reviewsWrittenCount", reviewsService.getReviewsCountUserWrote(userId));

        return "profile";
    }
    
    @GetMapping("edit")
    public String getEditProfilePage(Model model) {
        model.addAttribute("title", "Изменение профиля");
        model.addAttribute("editForm", new ProfileEditForm());
        return "redactProfile";
    }

    @PostMapping("edit")
    public String editProfile(@Valid @ModelAttribute ProfileEditForm editForm,
                              BindingResult bindingResult,
                              Model model,
                              @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Изменение профиля");
            model.addAttribute("editForm", editForm);
            return "redactProfile";
        }
        else {
            UserDTO userDTO = modelMapper.map(userDetails.getUser(), UserDTO.class);
            UserDTO refreshedUserDTO = usersService.editProfile(editForm, userDTO);
            if (refreshedUserDTO.getUserStatus().equals(UserStatus.CONFIRMED)) {
                return "redirect:/profile";
            }
            else {
                signUpService.sendConfirmEmailToUser(refreshedUserDTO);
                return "redirect:/signUp/profileEditConfirmEmail";
            }
        }
    }

}
