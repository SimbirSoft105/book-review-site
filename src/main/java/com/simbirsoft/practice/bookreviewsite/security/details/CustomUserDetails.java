package com.simbirsoft.practice.bookreviewsite.security.details;

import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    CustomUser getUser();
}
