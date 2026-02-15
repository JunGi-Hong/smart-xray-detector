package dna.safe_guard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Test
    void checkConfig() {
        System.out.println("설정된 ID: " + username);
        System.out.println("설정된 PW: " + password);
    }

    @Test
    @DisplayName("Email Send Test")
    void sendEmailTest() {

        String subject = "[SafeGate] 위해물품 탐지";
        String body = "위해물품 n개가 탐지되었습니다. SafeGate 게시판에서 확인 바랍니다.";
        String receiver = "Input Email Address";

        System.out.println("발송 요청 수신자: " + receiver);

        emailService.send(subject, body, receiver);

        System.out.println("메일 전송 요청 완료. 실제 수신함을 확인하세요.");
    }
}