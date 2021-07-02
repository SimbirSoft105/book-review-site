package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.enums.Role;
import com.simbirsoft.practice.bookreviewsite.enums.UserStatus;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;
import com.simbirsoft.practice.bookreviewsite.dto.SignUpForm;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import com.simbirsoft.practice.bookreviewsite.security.details.CustomUserDetails;
import com.simbirsoft.practice.bookreviewsite.util.AuthRefreshUtil;
import com.simbirsoft.practice.bookreviewsite.util.ConfirmMailGenerator;
import org.modelmapper.ModelMapper;
import com.simbirsoft.practice.bookreviewsite.service.EmailSendingService;
import com.simbirsoft.practice.bookreviewsite.service.SignUpService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SignUpServiceImpl implements SignUpService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailSendingService emailSendingService;

    private final ConfirmMailGenerator confirmMailGenerator;

    private final ModelMapper modelMapper;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public SignUpServiceImpl(UsersRepository usersRepository,
                             PasswordEncoder passwordEncoder,
                             EmailSendingService emailSendingService,
                             ConfirmMailGenerator confirmMailGenerator,
                             @Qualifier("customUserDetailService") UserDetailsService userDetailsService,
                             ModelMapper modelMapper) {

        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSendingService = emailSendingService;
        this.confirmMailGenerator = confirmMailGenerator;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserStatus devSignUpWithRole(SignUpForm signUpForm, Role role) {

        User user = User.builder()
                .name(signUpForm.getName())
                .email(signUpForm.getEmail())
                .hashedPassword(passwordEncoder.encode(signUpForm.getPassword()))
                .role(role)
                .confirmCode(UUID.randomUUID().toString())
                .userStatus(UserStatus.CONFIRMED)
                    .build();
        usersRepository.save(user);

        return user.getUserStatus();
    }

    @Override
    public UserDTO prodSignUpWithRole(SignUpForm signUpForm, Role role) {

        User user = User.builder()
                .name(signUpForm.getName())
                .email(signUpForm.getEmail())
                .hashedPassword(passwordEncoder.encode(signUpForm.getPassword()))
                .role(role)
                .confirmCode(UUID.randomUUID().toString())
                .userStatus(UserStatus.NOT_CONFIRMED)
                    .build();

        user = usersRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void confirmUserByConfirmCode(String confirmCode) throws UserNotFoundException {

        User user = usersRepository.getUserByConfirmCode(confirmCode)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setUserStatus(UserStatus.CONFIRMED);
        usersRepository.save(user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            userDetails.getUser().setUserStatus(UserStatus.CONFIRMED);
            AuthRefreshUtil.refreshAuthentication(authentication);
        }
    }

    @Override
    public boolean userWithSuchEmailExists(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Override
    public void sendConfirmEmailToUser(UserDTO userDTO) {
        String letter = confirmMailGenerator.generateConfirmMail(
                userDTO.getConfirmCode(),
                userDTO.getName()
        );

        emailSendingService.sendEmail(userDTO.getEmail(), letter,
                "Подтверждение email");
    }

}
