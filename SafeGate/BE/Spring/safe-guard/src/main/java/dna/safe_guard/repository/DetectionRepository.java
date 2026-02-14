package dna.safe_guard.repository;

import dna.safe_guard.entity.Detection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DetectionRepository extends JpaRepository<Detection, Long> {

    // eventId(게시글 번호)로 탐지 내역을 찾는 메서드
    // Optional<>: 결과가 없을 수도 있으니(null 방지) 안전하게 감싸서 반환
    Optional<Detection> findByEventId(Long eventId);
}