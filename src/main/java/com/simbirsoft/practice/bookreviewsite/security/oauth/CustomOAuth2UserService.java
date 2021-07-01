package com.simbirsoft.practice.bookreviewsite.security.oauth;

import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.enums.Role;
import com.simbirsoft.practice.bookreviewsite.enums.UserStatus;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import com.simbirsoft.practice.bookreviewsite.security.details.CustomUser;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Anvar Khasanov
 * student of ITIS KFU
 * group 11-905
 */

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;

    private final ModelMapper modelMapper;

    public CustomOAuth2UserService(UsersRepository usersRepository, ModelMapper modelMapper) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = getOrCreateUser(email, name);
        CustomUser customUser = modelMapper.map(user, CustomUser.class);

        return new CustomOAuth2User(oAuth2User, customUser);
    }

    private User getOrCreateUser(String email, String name) {
        Optional<User> optionalUser = usersRepository.getByEmail(email);

        if (!optionalUser.isPresent()) {

            User user = User.builder()
                    .email(email)
                    .name(name)
                    .role(Role.ROLE_USER)
                    .userStatus(UserStatus.CONFIRMED)
                    .build();

            return usersRepository.save(user);
        }

        return optionalUser.get();
    }
}
