package com.simbirsoft.practice.bookreviewsite.controller.prod;

import com.simbirsoft.practice.bookreviewsite.dto.ProfileEditForm;
import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;
import com.simbirsoft.practice.bookreviewsite.security.details.CustomUserDetails;
import com.simbirsoft.practice.bookreviewsite.service.api.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public String getProfilePage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) throws UserNotFoundException {

        UserDTO user = usersService.getById(userDetails.getUserDTO().getId());

        model.addAttribute("user", user);
        model.addAttribute("title", "Профиль");

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
                              @AuthenticationPrincipal CustomUserDetails userDetails) throws UserNotFoundException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Изменение профиля");
            model.addAttribute("editForm", editForm);
            return "redactProfile";
        }
        else {
            UserDTO userDTO = usersService.getById(userDetails.getUserDTO().getId());

            //Не беру юзера из UserDetails, потому что он не со свежими данными.
            //Нужен свежий аватар для удаления старой аватарки из облака по public_id
            usersService.editProfile(editForm, userDTO);
            return "redirect:/profile";
        }
    }

}
