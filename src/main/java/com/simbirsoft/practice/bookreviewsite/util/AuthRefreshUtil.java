package com.simbirsoft.practice.bookreviewsite.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthRefreshUtil {

    public static void refreshAuthentication(Authentication authentication) {

        Authentication refreshedAuth = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                authentication.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(refreshedAuth);
    }

}
