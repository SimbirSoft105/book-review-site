package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.simbirsoft.practice.bookreviewsite.dto.ProfileEditForm;
import com.simbirsoft.practice.bookreviewsite.dto.UserDTO;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import com.simbirsoft.practice.bookreviewsite.service.api.UsersService;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public void editProfile(ProfileEditForm profileEditForm, UserDTO userDTO) {

        String newName = profileEditForm.getName();
        String newEmail = profileEditForm.getEmail();
        String newAvatar;

        MultipartFile avatarFile = profileEditForm.getAvatar();
        if (avatarFile != null) {

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
    }

    @Override
    public UserDTO getById(Long id) throws UserNotFoundException{
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return modelMapper.map(user, UserDTO.class);
    }

}
