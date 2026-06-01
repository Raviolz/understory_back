package raviolz.understory_back.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.exceptions.InternalServerException;
import raviolz.understory_back.exceptions.ValidationException;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    private final Cloudinary cloudinaryUploader;

    public ImageUploadService(Cloudinary cloudinaryUploader) {
        this.cloudinaryUploader = cloudinaryUploader;
    }

    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("Image file is required");
        }

        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new ValidationException("Only image files are allowed");
        }

        try {
            Map<?, ?> result = cloudinaryUploader.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap()
            );

            return (String) result.get("secure_url");

        } catch (IOException ex) {
            throw new InternalServerException("Image upload failed");
        }
    }
}