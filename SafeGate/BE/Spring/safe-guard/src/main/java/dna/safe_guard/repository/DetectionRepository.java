package dna.safe_guard.repository;

import dna.safe_guard.entity.Detection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DetectionRepository extends JpaRepository<Detection, Long> {

    Optional<Detection> findByEventId(Long eventId);

    @Query("SELECT d FROM Detection d JOIN FETCH d.event e WHERE e.startTime >= :startTime")
    List<Detection> findAllWithEventByStartTimeAfter(@Param("startTime") String startTime);
}