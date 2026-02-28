// ReportController.java

package dna.safe_guard.controller;

import dna.safe_guard.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<?> getReport(@RequestParam("period") String period) {
        try {
            // Service에서 바이트 배열(PDF 파일 데이터)을 받아옴
            byte[] pdfData = statisticsService.generateAndSaveReport(period);

            // 다운로드될 파일 이름 설정
            String fileName = "SafeGate_" + period + "_report.pdf";

            // 브라우저가 PDF 파일로 인식하고 다운로드하도록 헤더 설정
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfData);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("fail", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Collections.singletonMap("fail", "리포트 생성 중 오류 발생"));
        }
    }
}