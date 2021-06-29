package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.enums.Role;
import com.simbirsoft.practice.bookreviewsite.enums.UserStatus;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;
import com.simbirsoft.practice.bookreviewsite.dto.SignUpForm;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import com.simbirsoft.practice.bookreviewsite.service.api.EmailSendingService;
import com.simbirsoft.practice.bookreviewsite.service.api.SignUpService;
import com.simbirsoft.practice.bookreviewsite.util.ConfirmMailGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSendingService emailSendingService;

    @Autowired
    private ConfirmMailGenerator confirmMailGenerator;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void signUpWithRole(SignUpForm signUpForm, Role role) {

        User user = User.builder()
                .name(signUpForm.getName())
                .email(signUpForm.getEmail())
                .hashedPassword(passwordEncoder.encode(signUpForm.getPassword()))
                .role(role)
                .confirmCode(UUID.randomUUID().toString())
                .build();

        UserStatus userStatus;
        if (activeProfile.equals("dev")) {
            userStatus = UserStatus.CONFIRMED;
        } else userStatus = UserStatus.NOT_CONFIRMED;

        user.setUserStatus(userStatus);
        usersRepository.save(user);

        if (activeProfile.equals("prod")) {
            String letter = confirmMailGenerator.generateConfirmMail(
                    user.getConfirmCode(),
                    user.getName()
            );
            emailSendingService.sendEmail(user.getEmail(), letter,
                    "Подтверждение email");
        }
    }

    @Override
    public void confirmUserByConfirmCode(String confirmCode) throws UserNotFoundException {

        User user = usersRepository.getUserByConfirmCode(confirmCode)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setUserStatus(UserStatus.CONFIRMED);
        usersRepository.save(user);
    }

    @Override
    public boolean userWithSuchEmailExists(String email) {
        return usersRepository.existsByEmail(email);
    }

}
