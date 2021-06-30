package com.simbirsoft.practice.bookreviewsite.service;

import com.simbirsoft.practice.bookreviewsite.dto.ProfileEditForm;
import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;

import java.io.IOException;

public interface UsersService {

    UserDTO editProfile(ProfileEditForm profileEditForm, UserDTO userDTO) throws IOException;

    UserDTO getById(Long id);
}
