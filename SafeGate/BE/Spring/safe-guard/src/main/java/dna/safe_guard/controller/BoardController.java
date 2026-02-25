package dna.safe_guard.controller;

import dna.safe_guard.dto.BoardDetailResponseDto;
import dna.safe_guard.dto.BoardPageResponseDto;
import dna.safe_guard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 1. 전체 게시글 조회
    @GetMapping("/{page-number}")
    public ResponseEntity<?> getBoardList(@PathVariable("page-number") int pageNumber) {
        try {
            if (pageNumber < 1) pageNumber = 1;
            BoardPageResponseDto response = boardService.getBoardList(pageNumber);

            if (response.getData().isEmpty()) {
                return ResponseEntity.status(400).body(Collections.singletonMap("fail", "no-data"));
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(Collections.singletonMap("fail", "internal error"));
        }
    }

    // 2. 상세 게시글 조회
    @GetMapping("/detail/{event-id}")
    public ResponseEntity<?> getBoardDetail(@PathVariable("event-id") Long eventId,
                                            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            BoardDetailResponseDto detail = boardService.getBoardDetail(eventId);
            return ResponseEntity.ok(detail);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Collections.singletonMap("fail", "no-data"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(Collections.singletonMap("fail", "internal error"));
        }
    }

    // 3. 최근 일주일치 원본 데이터 리스트 조회 (추가됨! 🚀)
    // BoardController.java
    @GetMapping("/statistics/recent")
    public ResponseEntity<?> getRecentTypeStatistics() {
        try {
            // 타입별 총 개수가 담긴 Map을 가져옵니다.
            Map<Integer, Integer> statistics = boardService.getRecentTypeCounts();

            if (statistics.isEmpty()) {
                return ResponseEntity.status(400).body(Collections.singletonMap("fail", "no-data"));
            }

            return ResponseEntity.ok(statistics); // JSON 객체 형태로 반환

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(Collections.singletonMap("fail", "internal error"));
        }
    }
}