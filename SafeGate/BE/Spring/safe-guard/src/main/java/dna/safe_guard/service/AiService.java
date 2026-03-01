package dna.safe_guard.service;

import dna.safe_guard.dto.AiDetectionItemDto;
import dna.safe_guard.dto.AiDetectionResponseDto;
import dna.safe_guard.entity.Detection;
import dna.safe_guard.entity.Event;
import dna.safe_guard.entity.Message;
import dna.safe_guard.entity.User;
import dna.safe_guard.repository.DetectionRepository;
import dna.safe_guard.repository.EventRepository;
import dna.safe_guard.repository.MessageRepository;
import dna.safe_guard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;
    private final EventRepository eventRepository;
    private final DetectionRepository detectionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final MessageRepository messageRepository;

    @Value("${django.api.url}")
    private String djangoApiUrl;

    @Value("${file.upload.directory}")
    private String uploadDir;

    // React가 이미지에 접근할 때 사용할 URL 프리픽스 (필요시 변경)
    private static final String IMAGE_URL_PREFIX = "http://localhost:8080/images/";

    public AiDetectionResponseDto analyzeImageWithDjango(String localFilePath) {
        log.info("Django 서버로 이미지 전송 시작. 로컬 경로: {}", localFilePath);

        try {
            File imageFile = new File(localFilePath);
            if (!imageFile.exists()) {
                throw new IllegalArgumentException("저장된 이미지를 찾을 수 없습니다: " + localFilePath);
            }

            HttpHeaders headers = new HttpHeaders();

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new FileSystemResource(imageFile));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<AiDetectionResponseDto> response = restTemplate.postForEntity(
                    djangoApiUrl,
                    requestEntity,
                    AiDetectionResponseDto.class
            );

            AiDetectionResponseDto resultDto = response.getBody();

            if (resultDto != null && resultDto.getImageBase64() != null) {
                String savedFileName = saveBase64Image(resultDto.getImageBase64(), imageFile.getName());
                resultDto.setImageBase64(null);
                resultDto.setImageFile(IMAGE_URL_PREFIX + savedFileName);

                log.info("Base64 이미지 변환 저장 완료: {}", savedFileName);
            }

            log.info("Django 분석 완료. 상태 코드: {}", response.getStatusCode());
            return resultDto;

        } catch (Exception e) {
            log.error("Django 통신 중 에러 발생", e);
            throw new RuntimeException("AI 분석 서버 연동 실패", e);
        }
    }

    private String saveBase64Image(String base64String, String originalFileName) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);

            String extension = "";
            String nameWithoutExtension = originalFileName;

            int dotIndex = originalFileName.lastIndexOf('.');

            if (dotIndex != -1) {
                extension = originalFileName.substring(dotIndex);
                nameWithoutExtension = originalFileName.substring(0, dotIndex);
            }

            String newFileName = nameWithoutExtension + "1" + extension;

            File targetFile = Paths.get(uploadDir, newFileName).toFile();

            try (OutputStream stream = new FileOutputStream(targetFile)) {
                stream.write(decodedBytes);
            }

            return newFileName;

        } catch (Exception e) {
            log.error("Base64 이미지 저장 실패", e);
            return originalFileName;
        }
    }

    @Transactional
    public Long saveDetectionResultToDB(String src, AiDetectionResponseDto aiResult, String userEmail) {
        String dynamicTitle = generateTitle(aiResult);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Event event = new Event();
        event.setStartTime(src);
        event.setTitle(dynamicTitle);
        event.setUser(user);
        event = eventRepository.save(event);

        Detection detection = new Detection();
        detection.setEvent(event);

        if (aiResult.getDetections() != null && !aiResult.getDetections().isEmpty()) {
            for (AiDetectionItemDto itemDto : aiResult.getDetections()) {
                mapNameToType(detection, itemDto.getItem());
            }
        }

        detectionRepository.save(detection);
        log.info("DB 저장 완료: Event ID = {}, 제목 = {}", event.getId(), dynamicTitle);

        return event.getId();
        /* 탐지된 경우 사용자에게 events 테이블 저장 및 메일 전송
        if (aiResult.getTotalDetected() != null && aiResult.getTotalDetected() > 0) {
            sendAlertEmail(aiResult, event);
        }
         */
    }

    private void sendAlertEmail(AiDetectionResponseDto aiResult, Event event) {
        int total = aiResult.getTotalDetected();
        
        List<String> uniqueKoreanNames = aiResult.getDetections().stream()
                .map(dto -> getKoreanName(dto.getItem()))
                .distinct()
                .collect(Collectors.toList());

        String textName;
        if (uniqueKoreanNames.size() == 1) {
            textName = uniqueKoreanNames.get(0);
        } else {
            textName = uniqueKoreanNames.get(0) + ", " + uniqueKoreanNames.get(1);
        }

        String body;
        if (total == 1) {
            body = String.format("위해물품[%s]이(가) 탐지되었습니다. SafeGate 게시판에서 확인 바랍니다.", textName);
        } else {
            body = String.format("위해물품 [%s] 외 총 %d개가 탐지되었습니다. SafeGate 게시판에서 확인 바랍니다.", textName, total);
        }

        Message message = new Message();
        message.setContent(body);
        message.setEvent(event);
        messageRepository.save(message);

        String subject = "[SafeGate] 위해물품 탐지 경고";

        List<String> receiverEmails = userRepository.findAll().stream()
                .map(User::getEmail)
                .filter(email -> email != null && email.contains("@"))
                .collect(Collectors.toList());

        if (!receiverEmails.isEmpty()) {
            try {
                emailService.send(subject, body, receiverEmails.toArray(new String[0]));
                log.info("{} 명의 사용자에게 탐지 알림 메일을 발송했습니다.", receiverEmails.size());
            } catch (Exception e) {
                log.error("메일 발송 중 오류가 발생했습니다.", e);
            }
        } else {
            log.warn("탐지 알림을 보낼 유효한 수신자 이메일이 없습니다.");
        }
    }

    private String generateTitle(AiDetectionResponseDto aiResult) {
        int total = aiResult.getTotalDetected() != null ? aiResult.getTotalDetected() : 0;

        if (total == 0 || aiResult.getDetections() == null || aiResult.getDetections().isEmpty()) {
            return "탐지된 위해물품 없음";
        }

        List<String> uniqueKoreanNames = aiResult.getDetections().stream()
                .map(dto -> getKoreanName(dto.getItem()))
                .distinct()
                .collect(Collectors.toList());

        if (total == 1) {
            return uniqueKoreanNames.get(0) + " 탐지";
        } else if (total == 2) {
            if (uniqueKoreanNames.size() == 1) {
                return uniqueKoreanNames.get(0) + " 총 2건 탐지";
            } else {
                return uniqueKoreanNames.get(0) + ", " + uniqueKoreanNames.get(1) + " 탐지";
            }
        } else {
            if (uniqueKoreanNames.size() == 1) {
                return uniqueKoreanNames.get(0) + " 총 " + total + "건 탐지";
            } else {
                return uniqueKoreanNames.get(0) + ", " + uniqueKoreanNames.get(1) + " 외 총 " + total + "건 탐지";
            }
        }
    }

    private String getKoreanName(String englishName) {
        if (englishName == null) return "알 수 없는 물품";

        switch (englishName.toLowerCase()) {
            case "aerosol": return "에어로졸";
            case "alcohol": return "알코올";
            case "awl": return "송곳";
            case "axe": return "도끼";
            case "bat": return "야구배트";
            case "battery": return "배터리";
            case "bullet": return "총알";
            case "chisel": return "끌";
            case "electronic cigarettes": return "전자담배";
            case "electronic cigarettes(liquid)": return "액상 전자담배";
            case "firecracker": return "폭죽";
            case "gun": return "총";
            case "gunparts": return "총기부품";
            case "hdd": return "하드디스크";
            case "hammer": return "망치";
            case "handcuffs": return "수갑";
            case "knife": return "칼";
            case "laptop": return "노트북";
            case "lighter": return "라이터";
            case "liquid": return "액체";
            case "match": return "성냥";
            case "metalpipe": return "금속파이프";
            case "nailclippers": return "손톱깎이";
            case "plier": return "플라이어";
            case "prtablegas": return "휴대용가스";
            case "ssd": return "SSD";
            case "saw": return "톱";
            case "scissors": return "가위";
            case "screwdriver": return "드라이버";
            case "smartphone": return "스마트폰";
            case "solidfuel": return "고체연료";
            case "spanner": return "스패너";
            case "supplymentarybattery": return "보조배터리";
            case "tabletpc": return "태블릿PC";
            case "thinner": return "시너";
            case "throwing knife": return "표창";
            case "usb": return "USB";
            case "zippooil": return "라이터오일";
            default: return englishName;
        }
    }

    private void mapNameToType(Detection detection, String itemName) {
        if (itemName == null) return;

        switch (itemName.toLowerCase()) {
            case "aerosol": detection.setType0(getOrDefault(detection.getType0()) + 1); break;
            case "alcohol": detection.setType1(getOrDefault(detection.getType1()) + 1); break;
            case "awl": detection.setType2(getOrDefault(detection.getType2()) + 1); break;
            case "axe": detection.setType3(getOrDefault(detection.getType3()) + 1); break;
            case "bat": detection.setType4(getOrDefault(detection.getType4()) + 1); break;
            case "battery": detection.setType5(getOrDefault(detection.getType5()) + 1); break;
            case "bullet": detection.setType6(getOrDefault(detection.getType6()) + 1); break;
            case "chisel": detection.setType7(getOrDefault(detection.getType7()) + 1); break;
            case "electronic cigarettes": detection.setType8(getOrDefault(detection.getType8()) + 1); break;
            case "electronic cigarettes(liquid)": detection.setType9(getOrDefault(detection.getType9()) + 1); break;
            case "firecracker": detection.setType10(getOrDefault(detection.getType10()) + 1); break;
            case "gun": detection.setType11(getOrDefault(detection.getType11()) + 1); break;
            case "gunparts": detection.setType12(getOrDefault(detection.getType12()) + 1); break;
            case "hdd": detection.setType13(getOrDefault(detection.getType13()) + 1); break;
            case "hammer": detection.setType14(getOrDefault(detection.getType14()) + 1); break;
            case "handcuffs": detection.setType15(getOrDefault(detection.getType15()) + 1); break;
            case "knife": detection.setType16(getOrDefault(detection.getType16()) + 1); break;
            case "laptop": detection.setType17(getOrDefault(detection.getType17()) + 1); break;
            case "lighter": detection.setType18(getOrDefault(detection.getType18()) + 1); break;
            case "liquid": detection.setType19(getOrDefault(detection.getType19()) + 1); break;
            case "match": detection.setType20(getOrDefault(detection.getType20()) + 1); break;
            case "metalpipe": detection.setType21(getOrDefault(detection.getType21()) + 1); break;
            case "nailclippers": detection.setType22(getOrDefault(detection.getType22()) + 1); break;
            case "plier": detection.setType23(getOrDefault(detection.getType23()) + 1); break;
            case "prtablegas": detection.setType24(getOrDefault(detection.getType24()) + 1); break;
            case "ssd": detection.setType25(getOrDefault(detection.getType25()) + 1); break;
            case "saw": detection.setType26(getOrDefault(detection.getType26()) + 1); break;
            case "scissors": detection.setType27(getOrDefault(detection.getType27()) + 1); break;
            case "screwdriver": detection.setType28(getOrDefault(detection.getType28()) + 1); break;
            case "smartphone": detection.setType29(getOrDefault(detection.getType29()) + 1); break;
            case "solidfuel": detection.setType30(getOrDefault(detection.getType30()) + 1); break;
            case "spanner": detection.setType31(getOrDefault(detection.getType31()) + 1); break;
            case "supplymentarybattery": detection.setType32(getOrDefault(detection.getType32()) + 1); break;
            case "tabletpc": detection.setType33(getOrDefault(detection.getType33()) + 1); break;
            case "thinner": detection.setType34(getOrDefault(detection.getType34()) + 1); break;
            case "throwing knife": detection.setType35(getOrDefault(detection.getType35()) + 1); break;
            case "usb": detection.setType36(getOrDefault(detection.getType36()) + 1); break;
            case "zippooil": detection.setType37(getOrDefault(detection.getType37()) + 1); break;
            default:
                log.warn("알 수 없는 탐지 항목입니다: {}", itemName);
                break;
        }
    }

    private int getOrDefault(Integer val) {
        return val == null ? 0 : val;
    }
}
