package dna.safe_guard.service;

import dna.safe_guard.dto.BoardDetailResponseDto;
import dna.safe_guard.dto.BoardListResponseDto;
import dna.safe_guard.dto.BoardPageResponseDto;
import dna.safe_guard.entity.Detection;
import dna.safe_guard.entity.Event;
import dna.safe_guard.repository.DetectionRepository;
import dna.safe_guard.repository.EventRepository;
import dna.safe_guard.dto.RecentEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final EventRepository eventRepository;
    private final DetectionRepository detectionRepository;

    // 이미지 경로 (나중에 배포 시 주소 변경 필요!!!)
    private static final String IMAGE_URL_PREFIX = "http://localhost:8080/images/";

    // =========================================================
    // 1. 목록 조회 (기존 유지)
    // =========================================================
    @Transactional(readOnly = true)
    public BoardPageResponseDto getBoardList(int pageNumber) {
        int pageIndex = (pageNumber < 1) ? 0 : pageNumber - 1;
        Pageable pageable = PageRequest.of(pageIndex, 10, Sort.by("id").descending());

        Page<Event> eventPage = eventRepository.findAll(pageable);

        List<BoardListResponseDto> content = eventPage.getContent().stream()
                .map(event -> BoardListResponseDto.builder()
                        .eventId(event.getId())
                        .startTime(event.getStartTime())
                        .userName(event.getUser() != null ? event.getUser().getName() : "Unknown")
                        .title(event.getTitle())
                        .build())
                .collect(Collectors.toList());

        return BoardPageResponseDto.builder()
                .data(content)
                .pageInfo(BoardPageResponseDto.PageInfo.builder()
                        .page(pageNumber)
                        .size(eventPage.getSize())
                        .totalElements(eventPage.getTotalElements())
                        .totalPages(eventPage.getTotalPages())
                        .build())
                .build();
    }

    // =========================================================
    // 2. 상세 조회 (기존 유지)
    // =========================================================
    @Transactional(readOnly = true)
    public BoardDetailResponseDto getBoardDetail(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + eventId));

        Detection detection = detectionRepository.findByEventId(eventId)
                .orElseThrow(() -> new IllegalArgumentException("탐지 데이터가 없습니다."));

        String timeStr = event.getStartTime();
        String fileName = "default.png";

        if (timeStr != null && !timeStr.isEmpty()) {
            fileName = timeStr.replace("-", "")
                    .replace(":", "")
                    .replace(" ", "")
                    + ".png";
        }

        return BoardDetailResponseDto.builder()
                .src(IMAGE_URL_PREFIX + fileName)
                .startTime(event.getStartTime())
                .detect(extractDetectedList(detection))
                .build();
    }

    // =========================================================
    // 3. 최근 7일 데이터 조회 (새로 추가! 🚀)
    // =========================================================
    @Transactional(readOnly = true)
    public List<RecentEventDto> getRecentEventList() { // 👈 타입을 List<Event>에서 List<RecentEventDto>로 변경
        String sevenDaysAgo = LocalDateTime.now().minusDays(7)
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        List<Event> events = eventRepository.findAllByStartTimeGreaterThanEqual(sevenDaysAgo);

        // 엔티티를 DTO로 변환하는 과정이 반드시 필요합니다!
        return events.stream().map(event -> {
            List<Integer> detectList = detectionRepository.findByEventId(event.getId())
                    .map(this::extractDetectedList)
                    .orElse(new java.util.ArrayList<>());

            return RecentEventDto.builder()
                    .id(event.getId())
                    .startTime(event.getStartTime())
                    .title(event.getTitle())
                    .detect(detectList)
                    .build();
        }).collect(java.util.stream.Collectors.toList());
    }

    // =========================================================
    // 4. 보조 메서드들
    // =========================================================
    private List<Integer> extractDetectedList(Detection d) {
        List<Integer> list = new ArrayList<>();
        // ... (기존 isValid 체크 로직들 0~37번 그대로 유지)
        if (isValid(d.getType0())) list.add(0);
        if (isValid(d.getType1())) list.add(1);
        if (isValid(d.getType2())) list.add(2);
        if (isValid(d.getType3())) list.add(3);
        if (isValid(d.getType4())) list.add(4);
        if (isValid(d.getType5())) list.add(5);
        if (isValid(d.getType6())) list.add(6);
        if (isValid(d.getType7())) list.add(7);
        if (isValid(d.getType8())) list.add(8);
        if (isValid(d.getType9())) list.add(9);
        if (isValid(d.getType10())) list.add(10);
        if (isValid(d.getType11())) list.add(11);
        if (isValid(d.getType12())) list.add(12);
        if (isValid(d.getType13())) list.add(13);
        if (isValid(d.getType14())) list.add(14);
        if (isValid(d.getType15())) list.add(15);
        if (isValid(d.getType16())) list.add(16);
        if (isValid(d.getType17())) list.add(17);
        if (isValid(d.getType18())) list.add(18);
        if (isValid(d.getType19())) list.add(19);
        if (isValid(d.getType20())) list.add(20);
        if (isValid(d.getType21())) list.add(21);
        if (isValid(d.getType22())) list.add(22);
        if (isValid(d.getType23())) list.add(23);
        if (isValid(d.getType24())) list.add(24);
        if (isValid(d.getType25())) list.add(25);
        if (isValid(d.getType26())) list.add(26);
        if (isValid(d.getType27())) list.add(27);
        if (isValid(d.getType28())) list.add(28);
        if (isValid(d.getType29())) list.add(29);
        if (isValid(d.getType30())) list.add(30);
        if (isValid(d.getType31())) list.add(31);
        if (isValid(d.getType32())) list.add(32);
        if (isValid(d.getType33())) list.add(33);
        if (isValid(d.getType34())) list.add(34);
        if (isValid(d.getType35())) list.add(35);
        if (isValid(d.getType36())) list.add(36);
        if (isValid(d.getType37())) list.add(37);

        return list;
    }

    private boolean isValid(Integer value) {
        return value != null && value > 0;
    }
}