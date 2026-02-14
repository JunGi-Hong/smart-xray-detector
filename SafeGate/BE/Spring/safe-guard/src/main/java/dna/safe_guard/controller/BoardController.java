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

    // [중요] 토큰 검증 로직을 위한 유틸리티 (본인 프로젝트에 있는 JWT 검증 클래스를 주입받아야 함)
    // private final JwtUtil jwtUtil; // <-- 만약 JWT 검증 클래스가 있다면 주석 풀고 사용

    // 1. 전체 게시글 조회 (토큰 검사 추가)
    @GetMapping("/{page-number}")
    public ResponseEntity<?> getBoardList(@PathVariable("page-number") int pageNumber,
                                          @RequestHeader(value = "Authorization", required = false) String token) { // 👈 토큰 받기!
        try {
            // [1] 토큰 검사: 토큰이 아예 없으면 "로그인 필요" 에러
            //if (token == null || token.isEmpty()) {
              //  return ResponseEntity.status(401).body(Collections.singletonMap("fail", "login-required"));
           // }


            // [3] 기존 로직 실행
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
            // [1] 토큰 검사
           // if (token == null || token.isEmpty()) {
           //     return ResponseEntity.status(401).body(Collections.singletonMap("fail", "login-required"));
           // }


            // [3] 기존 로직 실행
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