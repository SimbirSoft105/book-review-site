package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.simbirsoft.practice.bookreviewsite.dto.ProfileEditForm;
import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import com.simbirsoft.practice.bookreviewsite.security.details.CustomUserDetails;
import com.simbirsoft.practice.bookreviewsite.service.UsersService;
import com.simbirsoft.practice.bookreviewsite.util.AuthRefreshUtil;
import com.simbirsoft.practice.bookreviewsite.util.MediaFileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final ModelMapper modelMapper;

    private final MediaFileUtils mediaFileUtils;

    public UsersServiceImpl(UsersRepository usersRepository, ModelMapper modelMapper,
                            MediaFileUtils mediaFileUtils) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.mediaFileUtils = mediaFileUtils;
    }

    @Override
    public void editProfile(ProfileEditForm profileEditForm, UserDTO userDTO) {

        String newName = profileEditForm.getName();
        String newEmail = profileEditForm.getEmail();
        String newAvatar = userDTO.getAvatar();

        MultipartFile avatarFile = profileEditForm.getAvatar();
        if (avatarFile != null) {

            String currentAvatar = userDTO.getAvatar();
            if (currentAvatar != null) {
                mediaFileUtils.deleteFile(currentAvatar);
            }

            try {
                newAvatar = mediaFileUtils.uploadFile(
                        avatarFile.getOriginalFilename(), avatarFile.getBytes());
            } catch (IOException e) {
                throw new IllegalStateException();
            }

        }

        usersRepository.editProfile(newName, newEmail, newAvatar);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        user.setAvatar(newAvatar);
        user.setName(newName);
        user.setEmail(newEmail);

        AuthRefreshUtil.refreshAuthentication(authentication);
    }

    @Override
    public UserDTO getById(Long id) {
        User user = usersRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found"));

        return modelMapper.map(user, UserDTO.class);
    }

}
