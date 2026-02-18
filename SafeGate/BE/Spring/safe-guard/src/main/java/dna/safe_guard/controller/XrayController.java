package dna.safe_guard.controller;

import dna.safe_guard.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/x-ray")
@RequiredArgsConstructor
public class XrayController {

    private final ImageService imageService;

    @PostMapping("/image-upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("src") String src // 명세서의 src (시간 문자열)
    ) {
        try {
            // Service로 파일과 파일명(시간) 전달
            imageService.uploadImage(image, src);

            // 성공 시 200 OK & JSON 반환
            return ResponseEntity.ok(Collections.singletonMap("message", "success"));

        } catch (IllegalArgumentException e) {
            // 확장자가 안 맞거나 파일이 없을 때 (400 Bad Request)
            return ResponseEntity.status(400).body(Collections.singletonMap("fail", e.getMessage()));

        } catch (IOException e) {
            // 서버 저장 중 에러 (500 Internal Server Error)
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("fail", "server error"));
        }
    }

    // 파일 용량 초과 시 자동 실행되는 예외 핸들러
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(400).body(Collections.singletonMap("fail", "size limit exceeded"));
    }
}