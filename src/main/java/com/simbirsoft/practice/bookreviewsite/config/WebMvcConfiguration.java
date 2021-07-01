package com.simbirsoft.practice.bookreviewsite.config;

import com.simbirsoft.practice.bookreviewsite.interceptor.PastUserInTemplateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Anvar Khasanov
 * student of ITIS KFU
 * group 11-905
 */

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private PastUserInTemplateInterceptor userInTemplateInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInTemplateInterceptor);
    }
}
