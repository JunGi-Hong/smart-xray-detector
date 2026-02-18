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

    // application.properties에서 경로를 가져옵니다.
    @Value("${file.upload.directory}")
    private String uploadDir;

    public void uploadImage(MultipartFile image, String fileNameFromClient) throws IOException {

        // 1. 파일 존재 여부 확인
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        // 2. 확장자 유효성 검사 (이미지 파일만 허용)
        String originalFilename = image.getOriginalFilename();
        if (!isValidExtension(originalFilename)) {
            throw new IllegalArgumentException("invalid extension");
        }

        // 3. 저장소 폴더 생성 (없으면 자동 생성)
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("디렉토리 생성 실패: " + uploadDir);
            }
        }

        // 4. 파일명 생성 로직 (UUID 대신 시간 기반)
        // Client가 보낸 src(예: 20260219120000)를 그대로 사용해야
        // 나중에 조회(GET)할 때 DB 시간과 매칭됩니다.

        // 원본 파일에서 확장자 추출 (.png, .jpg)
        String ext = extractExtension(originalFilename);

        // 클라이언트가 보낸 src에 확장자가 없으면 붙여주고, 있으면 그대로 사용
        String finalFileName;
        if (fileNameFromClient.endsWith(ext)) {
            finalFileName = fileNameFromClient;
        } else {
            finalFileName = fileNameFromClient + ext;
        }

        // 5. 실제 파일 저장
        String fullPath = uploadDir + finalFileName;
        image.transferTo(new File(fullPath));
    }

    // [내부 메서드] 확장자 검사 (.jpg, .jpeg, .png 만 허용)
    private boolean isValidExtension(String filename) {
        if (filename == null) return false;
        String ext = extractExtension(filename).toLowerCase();
        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png");
        return allowedExtensions.contains(ext);
    }

    // [내부 메서드] 파일명에서 확장자 추출 (예: .png)
    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}