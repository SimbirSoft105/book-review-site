package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.simbirsoft.practice.bookreviewsite.dto.ProfileEditForm;
import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.enums.UserStatus;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import com.simbirsoft.practice.bookreviewsite.security.details.CustomUserDetails;
import com.simbirsoft.practice.bookreviewsite.service.UsersService;
import com.simbirsoft.practice.bookreviewsite.util.AuthRefreshUtil;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

    public UsersServiceImpl(UsersRepository usersRepository, ModelMapper modelMapper, Cloudinary cloudinary) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
    }

    @Override
    public UserDTO editProfile(ProfileEditForm profileEditForm, UserDTO userDTO) throws IOException {

        String newName = profileEditForm.getName();
        String newEmail = profileEditForm.getEmail();
        String newAvatar;

        MultipartFile avatarFile = profileEditForm.getAvatar();
        if (!avatarFile.getOriginalFilename().equals("")) {

            String currentAvatar = userDTO.getAvatar();
            if (currentAvatar != null) {
                String publicId = currentAvatar.substring(
                        currentAvatar.lastIndexOf("/") + 1,
                        currentAvatar.lastIndexOf("."));
                try {
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            try {
                File fileToUpload = new File(Objects.requireNonNull(avatarFile.getOriginalFilename()));
                byte[] bytes = avatarFile.getBytes();
                FileUtils.writeByteArrayToFile(fileToUpload, bytes);
                Map response = cloudinary.uploader().upload(fileToUpload, ObjectUtils.emptyMap());
                newAvatar = (String) response.get("url");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        else newAvatar = userDTO.getAvatar();

        usersRepository.editProfile(newName, newEmail, newAvatar);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        user.setAvatar(newAvatar);
        user.setName(newName);
        user.setEmail(newEmail);

        if (!userDTO.getEmail().equals(newEmail)) {
            user.setUserStatus(UserStatus.NOT_CONFIRMED);
            user.setConfirmCode(UUID.randomUUID().toString());
            usersRepository.makeUserNotConfirmed(user.getConfirmCode(),
                    user.getUserStatus());
            System.out.println("new confirmCode: " + user.getConfirmCode());
        }
        AuthRefreshUtil.refreshAuthentication(authentication);

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO getById(Long id) {
        User user = usersRepository.findById(id).get();
        return modelMapper.map(user, UserDTO.class);
    }

}
