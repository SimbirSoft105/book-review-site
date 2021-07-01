package com.simbirsoft.practice.bookreviewsite.security.config;

import com.simbirsoft.practice.bookreviewsite.security.filters.UserConfirmedFilter;
import com.simbirsoft.practice.bookreviewsite.security.oauth.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final UserConfirmedFilter userConfirmedFilter;

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfiguration(@Qualifier("customUserDetailService") UserDetailsService userDetailsService,
                                 PasswordEncoder passwordEncoder, UserConfirmedFilter userConfirmedFilter,
                                 CustomOAuth2UserService customOAuth2UserService) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userConfirmedFilter = userConfirmedFilter;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/images/**", "/sign_up/**",
                        "/login/**").permitAll()
                .antMatchers("/profile/**").authenticated()
                .antMatchers("/book/my").authenticated()
                .antMatchers("/book/delete/**").authenticated()
                .antMatchers("/book/add").authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error")
                    .usernameParameter("email")
                    .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint().userService(customOAuth2UserService)
                    .and()
                    .defaultSuccessUrl("/")
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                .and()
                    .addFilterAfter(userConfirmedFilter, UsernamePasswordAuthenticationFilter.class)
                    //.anonymous().disable()
                    .csrf().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
