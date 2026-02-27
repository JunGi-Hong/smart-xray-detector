package dna.safe_guard.repository;

import dna.safe_guard.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository를 상속받으면 DB에 CRUD(저장, 조회, 수정, 삭제)하는 코드를 안 짜도 자동 생성됨
public interface EventRepository extends JpaRepository<Event, Long> {

    // 1. 페이징 처리된 목록 조회 (기존 게시판용)
    // Pageable을 넘기면 알아서 LIMIT, OFFSET 쿼리를 날려줌
    Page<Event> findAll(Pageable pageable);

    // 2. 최근 7일치 원본 데이터 조회
    // start_time이 파라미터로 받은 문자열보다 크거나 같은 데이터를 모두 리스트로 가져옵니다.
    List<Event> findAllByStartTimeGreaterThanEqual(String startTime);
}