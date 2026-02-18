package dna.safe_guard.controller;

import dna.safe_guard.dto.BoardDetailResponseDto;
import dna.safe_guard.dto.BoardPageResponseDto;
import dna.safe_guard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 1. 전체 게시글 조회 (토큰 검사 추가)
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

    // 2. 상세 게시글 조회 (토큰 검사 추가)
    @GetMapping("/detail/{event-id}")
    public ResponseEntity<?> getBoardDetail(@PathVariable("event-id") Long eventId,
                                            @RequestHeader(value = "Authorization", required = false) String token) { // 👈 토큰 받기!
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
}