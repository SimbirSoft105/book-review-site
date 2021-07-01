package com.simbirsoft.practice.bookreviewsite.config;

import com.simbirsoft.practice.bookreviewsite.interceptor.PastUserInTemplateInterceptor;
import org.modelmapper.ModelMapper;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EntityScan(basePackages = "com.simbirsoft.practice.bookreviewsite.entity")
@ComponentScan(basePackages = "com.simbirsoft.practice.bookreviewsite")
@EnableJpaRepositories(basePackages = "com.simbirsoft.practice.bookreviewsite.repository")
public class WebAppConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Cloudinary cloudinary() {

        Map<String, Object> cloudinaryConfig = new HashMap<>();

        cloudinaryConfig.put("cloud_name", environment.getProperty("cloudinary.properties.cloud_name"));
        cloudinaryConfig.put("api_key", environment.getProperty("cloudinary.properties.api_key"));
        cloudinaryConfig.put("api_secret", environment.getProperty("cloudinary.properties.api_secret"));
        cloudinaryConfig.put("secure", environment.getProperty("cloudinary.properties.secure"));

        return new Cloudinary(cloudinaryConfig);

    }
}
