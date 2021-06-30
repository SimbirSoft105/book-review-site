package com.simbirsoft.practice.bookreviewsite.service;

import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.enums.Role;
import com.simbirsoft.practice.bookreviewsite.enums.UserStatus;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;
import com.simbirsoft.practice.bookreviewsite.dto.SignUpForm;

public interface SignUpService {

    UserStatus devSignUpWithRole(SignUpForm signUpForm, Role role);

    UserDTO prodSignUpWithRole(SignUpForm signUpForm, Role role);

    void confirmUserByConfirmCode(String confirmCode) throws UserNotFoundException;

    boolean userWithSuchEmailExists(String email);

    void sendConfirmEmailToUser(UserDTO userDTO);

}
