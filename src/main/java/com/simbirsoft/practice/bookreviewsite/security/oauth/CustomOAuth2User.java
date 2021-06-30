package com.simbirsoft.practice.bookreviewsite.security.oauth;

import com.simbirsoft.practice.bookreviewsite.security.details.CustomUser;
import com.simbirsoft.practice.bookreviewsite.security.details.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @author Anvar Khasanov
 * student of ITIS KFU
 * group 11-905
 */

public class CustomOAuth2User implements OAuth2User, Serializable, CustomUserDetails {

    private static final long serialVersionUID = -6311927519298712654L;

    private final OAuth2User oAuth2User;
    
    private CustomUser user;

    public CustomOAuth2User(OAuth2User oAuth2User, CustomUser user) {
        this.oAuth2User = oAuth2User;
        this.user = user;
    }

    public CustomOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (user != null) {
            authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
            authorities.add(new SimpleGrantedAuthority(user.getUserStatus().toString()));
        }

        authorities.addAll(oAuth2User.getAuthorities());

        return authorities;
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    @Override
    public String getUsername() {
        return oAuth2User.getAttribute("email");
    }

    @Override
    public CustomUser getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }
}
