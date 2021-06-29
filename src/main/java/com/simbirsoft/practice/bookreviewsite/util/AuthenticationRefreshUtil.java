package com.simbirsoft.practice.bookreviewsite.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationRefreshUtil {

    public static void refreshAuthentication(Authentication authentication, UserDetails userDetails) {
        Authentication refreshedAuthentication =
                new UsernamePasswordAuthenticationToken(userDetails,
                        authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(refreshedAuthentication);
    }

}
