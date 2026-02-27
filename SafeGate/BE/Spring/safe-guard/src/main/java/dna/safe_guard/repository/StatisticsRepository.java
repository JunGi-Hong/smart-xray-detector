package dna.safe_guard.repository;

import dna.safe_guard.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
}