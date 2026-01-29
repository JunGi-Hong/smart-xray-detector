package dna.safe_guard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "messages")
@Getter @Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    // Events 테이블과의 관계 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    // 연관관계 편의 메서드
    public void setEvent(Event event) {
        this.event = event;
        if (event != null && !event.getMessages().contains(this)) {
            event.getMessages().add(this);
        }
    }
}
