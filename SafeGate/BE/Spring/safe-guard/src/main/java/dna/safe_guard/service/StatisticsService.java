package dna.safe_guard.service;

import dna.safe_guard.dto.ReportDto;
import dna.safe_guard.entity.Detection;
import dna.safe_guard.entity.Statistics;
import dna.safe_guard.repository.DetectionRepository;
import dna.safe_guard.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final DetectionRepository detectionRepository;
    private final StatisticsRepository statisticsRepository;
    private final RestTemplate restTemplate;

    @Value("${django.report.url}")
    private String djangoReportUrl;

    @Transactional
    public String generateAndSaveReport(String period) {
        LocalDate today = LocalDate.now();
        LocalDate start;

        if ("week".equalsIgnoreCase(period)) {
            start = today.minusDays(7);
        } else if ("month".equalsIgnoreCase(period)) {
            start = today.minusMonths(1);
        } else {
            throw new IllegalArgumentException("period는 'week' 또는 'month'만 가능합니다.");
        }

        String displayStartDate = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String displayEndDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String dbQueryStartTime = displayStartDate + " 00:00:00";

        List<Detection> detections = detectionRepository.findAllWithEventByStartTimeAfter(dbQueryStartTime);

        List<ReportDto.EventData> eventDataList = new ArrayList<>();

        for (Detection detection : detections) {
            List<String> items = extractDetectedKoreanNames(detection);
            if (!items.isEmpty()) {
                eventDataList.add(ReportDto.EventData.builder()
                        .time(detection.getEvent().getStartTime())
                        .detectedItems(items)
                        .build());
            }
        }

        ReportDto.Request requestDto = ReportDto.Request.builder()
                .period(period)
                .startDate(displayStartDate)
                .endDate(displayEndDate)
                .events(eventDataList)
                .build();

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDto);
            System.out.println("\n======================================");
            System.out.println("[Spring -> Django] 전송할 JSON 데이터 확인:");
            System.out.println(jsonStr);
            System.out.println("======================================\n");
        } catch (Exception e) {}

        log.info("Django 서버로 통계 리포트 생성 요청 시작... (기간: {})", period);

        ResponseEntity<ReportDto.Response> response = restTemplate.postForEntity(
                djangoReportUrl, requestDto, ReportDto.Response.class
        );

        if (response.getBody() == null || response.getBody().getPdfUrl() == null) {
            throw new RuntimeException("Django 서버에서 PDF를 생성하지 못했거나 URL을 반환하지 않았습니다.");
        }

        String pdfUrl = response.getBody().getPdfUrl();

        Statistics stat = new Statistics();
        stat.setAnalysisStartDate(displayStartDate);
        stat.setAnalysisEndDate(displayEndDate);
        stat.setDownloadUrl(pdfUrl);
        statisticsRepository.save(stat);

        return pdfUrl;
    }

    private List<String> extractDetectedKoreanNames(Detection d) {
        List<String> list = new ArrayList<>();
        addItems(list, "에어로졸", d.getType0());
        addItems(list, "알코올", d.getType1());
        addItems(list, "송곳", d.getType2());
        addItems(list, "도끼", d.getType3());
        addItems(list, "야구배트", d.getType4());
        addItems(list, "배터리", d.getType5());
        addItems(list, "총알", d.getType6());
        addItems(list, "끌", d.getType7());
        addItems(list, "전자담배", d.getType8());
        addItems(list, "액상 전자담배", d.getType9());
        addItems(list, "폭죽", d.getType10());
        addItems(list, "총", d.getType11());
        addItems(list, "총기부품", d.getType12());
        addItems(list, "하드디스크", d.getType13());
        addItems(list, "망치", d.getType14());
        addItems(list, "수갑", d.getType15());
        addItems(list, "칼", d.getType16());
        addItems(list, "노트북", d.getType17());
        addItems(list, "라이터", d.getType18());
        addItems(list, "액체", d.getType19());
        addItems(list, "성냥", d.getType20());
        addItems(list, "금속파이프", d.getType21());
        addItems(list, "손톱깎이", d.getType22());
        addItems(list, "플라이어", d.getType23());
        addItems(list, "휴대용가스", d.getType24());
        addItems(list, "SSD", d.getType25());
        addItems(list, "톱", d.getType26());
        addItems(list, "가위", d.getType27());
        addItems(list, "드라이버", d.getType28());
        addItems(list, "스마트폰", d.getType29());
        addItems(list, "고체연료", d.getType30());
        addItems(list, "스패너", d.getType31());
        addItems(list, "보조배터리", d.getType32());
        addItems(list, "태블릿PC", d.getType33());
        addItems(list, "시너", d.getType34());
        addItems(list, "표창", d.getType35());
        addItems(list, "USB", d.getType36());
        addItems(list, "라이터오일", d.getType37());
        return list;
    }

    private void addItems(List<String> list, String name, Integer count) {
        if (count != null && count > 0) {
            for (int i = 0; i < count; i++) {
                list.add(name);
            }
        }
    }
}