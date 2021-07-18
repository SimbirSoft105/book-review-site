package com.simbirsoft.practice.bookreviewsite.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Anvar Khasanov
 * student of ITIS KFU
 * group 11-905
 */

@Component
public class MediaFileUtils {

    private final Cloudinary cloudinary;

    public MediaFileUtils(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(@NotNull String fileName, byte[] bytes) {

        try {
            File fileToUpload = new File(fileName);
            FileUtils.writeByteArrayToFile(fileToUpload, bytes);
            Map response = cloudinary.uploader().upload(fileToUpload, ObjectUtils.emptyMap());
            String imgHref = (String) response.get("url");
            fileToUpload.delete();

            return imgHref;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void deleteFile(@NotNull String fileName) {
        try {
            String publicId = fileName.substring(
                    fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("."));

            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException ignore) {}
    }
}
