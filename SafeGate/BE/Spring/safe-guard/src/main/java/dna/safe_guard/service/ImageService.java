package dna.safe_guard.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ImageService {

    @Value("${file.upload.directory}")
    private String uploadDir;

    public String uploadImage(MultipartFile image, String fileNameFromClient) throws IOException {

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        String originalFilename = image.getOriginalFilename();
        if (!isValidExtension(originalFilename)) {
            throw new IllegalArgumentException("invalid extension");
        }

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("디렉토리 생성 실패: " + uploadDir);
            }
        }

        String ext = extractExtension(originalFilename);

        String finalFileName;
        if (fileNameFromClient.endsWith(ext)) {
            finalFileName = fileNameFromClient;
        } else {
            finalFileName = fileNameFromClient + ext;
        }

        String fullPath = uploadDir + finalFileName;
        image.transferTo(new File(fullPath));

        return fullPath;
    }

    private boolean isValidExtension(String filename) {
        if (filename == null) return false;
        String ext = extractExtension(filename).toLowerCase();
        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png");
        return allowedExtensions.contains(ext);
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}