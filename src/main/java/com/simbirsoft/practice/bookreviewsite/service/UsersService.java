package com.simbirsoft.practice.bookreviewsite.service;

import com.simbirsoft.practice.bookreviewsite.dto.ProfileEditForm;
import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;

public interface UsersService {

    void editProfile(ProfileEditForm profileEditForm, UserDTO userDTO);

    UserDTO getById(Long id);
}
