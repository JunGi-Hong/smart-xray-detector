package dna.safe_guard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "statistics")
@Getter @Setter
@NoArgsConstructor
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "analysis_start_date")
    private String analysisStartDate;

    @Column(name = "analysis_end_date")
    private String analysisEndDate;

    @Column(name = "download_url")
    private String downloadUrl;
}
