package dna.safe_guard.controller;

import dna.safe_guard.dto.AiDetectionResponseDto;
import dna.safe_guard.security.CustomUserDetails;
import dna.safe_guard.service.AiService;
import dna.safe_guard.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@RestController
@RequestMapping("/x-ray")
@RequiredArgsConstructor
public class XrayController {

    private final ImageService imageService;
    private final AiService aiService;

    @PostMapping("/image-upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        try {
            LocalDateTime now = LocalDateTime.now();
            String dbStartTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String fileNameTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String savedLocalPath = imageService.uploadImage(image, fileNameTime);
            AiDetectionResponseDto aiResult = aiService.analyzeImageWithDjango(savedLocalPath);
            aiService.saveDetectionResultToDB(dbStartTime, aiResult, userDetails.getEmail());

            return ResponseEntity.ok(aiResult);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap("fail", e.getMessage()));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("fail", "server image save error"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("fail", "ai server connection error"));
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(400).body(Collections.singletonMap("fail", "size limit exceeded"));
    }
}