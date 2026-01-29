package dna.safe_guard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter @Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private String startTime;

    // Users 테이블과의 관계 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Event가 삭제되면 연결된 Detections도 삭제됨
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detection> detections = new ArrayList<>();

    // Event가 삭제되면 연결된 Messages도 삭제됨
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    // 연관관계 편의 메서드 (User 설정 시 리스트에도 추가)
    public void setUser(User user) {
        this.user = user;
        if (user != null && !user.getEvents().contains(this)) {
            user.getEvents().add(this);
        }
    }
}
