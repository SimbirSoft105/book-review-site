package com.simbirsoft.practice.bookreviewsite.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ProfileEditForm {

    @NotBlank(message = "Данное поле не должно быть пустым")
    @Size(max = 50, message = "Длина имени не должна превышать 50 символов")
    private String name;

    @NotBlank(message = "Данное поле не должно быть пустым")
    @Email(message = "Неверный формат email адреса")
    private String email;

    private MultipartFile avatar;

}
