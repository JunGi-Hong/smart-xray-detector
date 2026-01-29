package dna.safe_guard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detections")
@Getter @Setter
@NoArgsConstructor
public class Detection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Events 테이블과의 관계 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    // type 1 ~ 27
    private Integer type1;
    private Integer type2;
    private Integer type3;
    private Integer type4;
    private Integer type5;
    private Integer type6;
    private Integer type7;
    private Integer type8;
    private Integer type9;
    private Integer type10;
    private Integer type11;
    private Integer type12;
    private Integer type13;
    private Integer type14;
    private Integer type15;
    private Integer type16;
    private Integer type17;
    private Integer type18;
    private Integer type19;
    private Integer type20;
    private Integer type21;
    private Integer type22;
    private Integer type23;
    private Integer type24;
    private Integer type25;
    private Integer type26;
    private Integer type27;

    // 연관관계 편의 메서드
    public void setEvent(Event event) {
        this.event = event;
        if (event != null && !event.getDetections().contains(this)) {
            event.getDetections().add(this);
        }
    }
}
