package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.simbirsoft.practice.bookreviewsite.dto.SignUpForm;
import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.enums.Role;
import com.simbirsoft.practice.bookreviewsite.enums.UserStatus;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import com.simbirsoft.practice.bookreviewsite.service.EmailSendingService;
import com.simbirsoft.practice.bookreviewsite.service.SignUpService;
import com.simbirsoft.practice.bookreviewsite.util.ConfirmMailGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = SignUpServiceImpl.class)
@ExtendWith(SpringExtension.class)
class SignUpServiceImplTest {

    @Autowired
    private SignUpService signUpService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailSendingService emailSendingService;

    @MockBean
    private ConfirmMailGenerator confirmMailGenerator;

    @MockBean
    @Qualifier("customUserDetailService")
    private UserDetailsService userDetailsService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void on_existing_email_return_true() {

        String correctEmail = "rosterfee@gmail.com";
        when(usersRepository.existsByEmail(correctEmail)).thenReturn(true);

        assertTrue(signUpService.userWithSuchEmailExists(correctEmail));
    }

    @Test
    void on_not_existing_email_return_false() {

        String wrongEmail = "romochkaBot@gmail.com";
        when(usersRepository.existsByEmail(wrongEmail)).thenReturn(false);

        assertFalse(signUpService.userWithSuchEmailExists(wrongEmail));
    }

    @Test
    void on_dev_profile_sign_up_user_confirmed() {

        SignUpForm signUpForm = SignUpForm.builder()
                .email("vasyaPupkin@gmail.com")
                .name("Вася Пупкин")
                .password("qwerty008")
                .verifyPassword("qwerty008")
                    .build();

        UserStatus userStatus = signUpService.devSignUpWithRole(signUpForm, Role.ROLE_USER);
        assertEquals(UserStatus.CONFIRMED, userStatus);
    }

    @Test
    void on_prod_profile_sign_up_user_not_confirmed() {

        SignUpForm signUpForm = SignUpForm.builder()
                .email("vasyaPupkin@gmail.com")
                .name("Вася Пупкин")
                .password("qwerty008")
                .verifyPassword("qwerty008")
                .build();

        UserDTO userDTO = signUpService.prodSignUpWithRole(signUpForm, Role.ROLE_USER);
        assertEquals(UserStatus.NOT_CONFIRMED, userDTO.getUserStatus());
    }
}