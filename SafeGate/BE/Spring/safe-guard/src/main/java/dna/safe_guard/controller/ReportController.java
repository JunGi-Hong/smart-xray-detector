package dna.safe_guard.controller;

import dna.safe_guard.service.StatisticsService;
import lombok.RequiredArgsConstructor;
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
            String downloadUrl = statisticsService.generateAndSaveReport(period);

            return ResponseEntity.ok(Collections.singletonMap("download_url", downloadUrl));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("fail", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Collections.singletonMap("fail", "리포트 생성 중 오류 발생"));
        }
    }
}