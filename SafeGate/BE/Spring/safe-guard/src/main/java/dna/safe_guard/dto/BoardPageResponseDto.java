package dna.safe_guard.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class BoardPageResponseDto {

    // 실제 게시글 10개가 들어갈 리스트
    private List<BoardListResponseDto> data;

    // 프론트엔드가 하단에 [1][2][3] 페이지 버튼을 그리기 위해 필요한 정보
    private PageInfo pageInfo;

    @Data
    @Builder
    public static class PageInfo {
        private int page;           // 현재 페이지 번호 (예: 1)
        private int size;           // 한 페이지당 보여줄 개수 (예: 10)
        private long totalElements; // 전체 게시글 총 개수 (예: 24개)
        private int totalPages;     // 전체 페이지 수 (예: 3페이지)
    }
}