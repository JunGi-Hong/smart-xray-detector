package dna.safe_guard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data // Getter, Setter, toString 등을 자동 생성해주는 롬복(Lombok) 어노테이션
@Builder // 객체를 생성할 때 new BoardListResponseDto(...) 대신 .builder().build()를 쓰게 해줌
public class BoardListResponseDto {

    // @JsonProperty: 자바 변수명은 'eventId'지만, JSON으로 나갈 땐 "event-id"로 이름표를 바꿔줌 (API 명세서 맞춤)
    @JsonProperty("event-id")
    private Long eventId;

    @JsonProperty("start-time")
    private String startTime;

    @JsonProperty("user-name") // UI 화면에 "홍길동" 이름을 띄우기 위해 추가한 필드
    private String userName;

    private String title; // 이건 명세서랑 자바 변수명이 같아서 @JsonProperty 생략 가능
}