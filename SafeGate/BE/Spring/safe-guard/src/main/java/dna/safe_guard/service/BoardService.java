package dna.safe_guard.service;

import dna.safe_guard.dto.BoardDetailResponseDto;
import dna.safe_guard.dto.BoardListResponseDto;
import dna.safe_guard.dto.BoardPageResponseDto;
import dna.safe_guard.dto.RecentEventDto;
import dna.safe_guard.entity.Detection;
import dna.safe_guard.entity.Event;
import dna.safe_guard.repository.DetectionRepository;
import dna.safe_guard.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final EventRepository eventRepository;
    private final DetectionRepository detectionRepository;

    private static final String IMAGE_URL_PREFIX = "http://localhost:8080/images/";

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
                    + "1.png";
        }

        return BoardDetailResponseDto.builder()
                .src(IMAGE_URL_PREFIX + fileName)
                .startTime(event.getStartTime())
                .detect(extractDetectedList(detection)) // 기존 배열 방식 유지
                .build();
    }

    @Transactional(readOnly = true)
    public List<RecentEventDto> getRecentEventList() {
        String sevenDaysAgo = LocalDate.now().minusDays(7)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";

        List<Detection> detections = detectionRepository.findAllWithEventByStartTimeAfter(sevenDaysAgo);

        return detections.stream().map(detection ->
                RecentEventDto.builder()
                        .detect(extractDetectedMap(detection))
                        .build()
        ).collect(Collectors.toList());
    }

    private Map<Integer, Integer> extractDetectedMap(Detection d) {
        Map<Integer, Integer> map = new HashMap<>();
        addTypeToMap(map, 0, d.getType0());
        addTypeToMap(map, 1, d.getType1());
        addTypeToMap(map, 2, d.getType2());
        addTypeToMap(map, 3, d.getType3());
        addTypeToMap(map, 4, d.getType4());
        addTypeToMap(map, 5, d.getType5());
        addTypeToMap(map, 6, d.getType6());
        addTypeToMap(map, 7, d.getType7());
        addTypeToMap(map, 8, d.getType8());
        addTypeToMap(map, 9, d.getType9());
        addTypeToMap(map, 10, d.getType10());
        addTypeToMap(map, 11, d.getType11());
        addTypeToMap(map, 12, d.getType12());
        addTypeToMap(map, 13, d.getType13());
        addTypeToMap(map, 14, d.getType14());
        addTypeToMap(map, 15, d.getType15());
        addTypeToMap(map, 16, d.getType16());
        addTypeToMap(map, 17, d.getType17());
        addTypeToMap(map, 18, d.getType18());
        addTypeToMap(map, 19, d.getType19());
        addTypeToMap(map, 20, d.getType20());
        addTypeToMap(map, 21, d.getType21());
        addTypeToMap(map, 22, d.getType22());
        addTypeToMap(map, 23, d.getType23());
        addTypeToMap(map, 24, d.getType24());
        addTypeToMap(map, 25, d.getType25());
        addTypeToMap(map, 26, d.getType26());
        addTypeToMap(map, 27, d.getType27());
        addTypeToMap(map, 28, d.getType28());
        addTypeToMap(map, 29, d.getType29());
        addTypeToMap(map, 30, d.getType30());
        addTypeToMap(map, 31, d.getType31());
        addTypeToMap(map, 32, d.getType32());
        addTypeToMap(map, 33, d.getType33());
        addTypeToMap(map, 34, d.getType34());
        addTypeToMap(map, 35, d.getType35());
        addTypeToMap(map, 36, d.getType36());
        addTypeToMap(map, 37, d.getType37());
        return map;
    }

    private void addTypeToMap(Map<Integer, Integer> map, int typeNumber, Integer count) {
        if (count != null && count > 0) {
            map.put(typeNumber, count);
        }
    }

    private List<Integer> extractDetectedList(Detection d) {
        List<Integer> list = new ArrayList<>();
        addTypeCount(list, 0, d.getType0());
        addTypeCount(list, 1, d.getType1());
        addTypeCount(list, 2, d.getType2());
        addTypeCount(list, 3, d.getType3());
        addTypeCount(list, 4, d.getType4());
        addTypeCount(list, 5, d.getType5());
        addTypeCount(list, 6, d.getType6());
        addTypeCount(list, 7, d.getType7());
        addTypeCount(list, 8, d.getType8());
        addTypeCount(list, 9, d.getType9());
        addTypeCount(list, 10, d.getType10());
        addTypeCount(list, 11, d.getType11());
        addTypeCount(list, 12, d.getType12());
        addTypeCount(list, 13, d.getType13());
        addTypeCount(list, 14, d.getType14());
        addTypeCount(list, 15, d.getType15());
        addTypeCount(list, 16, d.getType16());
        addTypeCount(list, 17, d.getType17());
        addTypeCount(list, 18, d.getType18());
        addTypeCount(list, 19, d.getType19());
        addTypeCount(list, 20, d.getType20());
        addTypeCount(list, 21, d.getType21());
        addTypeCount(list, 22, d.getType22());
        addTypeCount(list, 23, d.getType23());
        addTypeCount(list, 24, d.getType24());
        addTypeCount(list, 25, d.getType25());
        addTypeCount(list, 26, d.getType26());
        addTypeCount(list, 27, d.getType27());
        addTypeCount(list, 28, d.getType28());
        addTypeCount(list, 29, d.getType29());
        addTypeCount(list, 30, d.getType30());
        addTypeCount(list, 31, d.getType31());
        addTypeCount(list, 32, d.getType32());
        addTypeCount(list, 33, d.getType33());
        addTypeCount(list, 34, d.getType34());
        addTypeCount(list, 35, d.getType35());
        addTypeCount(list, 36, d.getType36());
        addTypeCount(list, 37, d.getType37());
        return list;
    }

    private void addTypeCount(List<Integer> list, int typeNumber, Integer count) {
        if (count != null && count > 0) {
            for (int i = 0; i < count; i++) {
                list.add(typeNumber);
            }
        }
    }

    private boolean isValid(Integer value) {
        return value != null && value > 0;
    }
}