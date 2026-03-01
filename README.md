<br>
<h1 align="center">🛡️ SafeGate 🛡️<span style="font-size:1.2em;"></span></h1>

<p align="center">
  <a href="https://spring.io/projects/spring-boot">
  <img src="https://img.shields.io/static/v1?label=Spring%20Boot&message=Backend&color=green&logo=springboot" alt="Spring Boot">
  </a>
  <a href="https://www.djangoproject.com/">
  <img src="https://img.shields.io/static/v1?label=Django&message=AI/PDF&color=darkgreen&logo=django" alt="Django">
  </a>
  <a href="https://www.mysql.com/">
  <img src="https://img.shields.io/static/v1?label=MySQL&message=Database&color=blue&logo=mysql" alt="MySQL">
  </a>
  <a href="https://github.com/ultralytics/ultralytics">
  <img src="https://img.shields.io/static/v1?label=YOLOv8&message=Vision%20AI&color=yellow&logo=yolo" alt="YOLOv8">
  </a>
  <a href="https://openai.com/">
  <img src="https://img.shields.io/static/v1?label=OpenAI&message=GPT-5-mini&color=black&logo=openai" alt="OpenAI">
  </a>
</p>

<p align="center">
  <b>X-ray 이미지 AI 분석 및 지능형 보안 검색 리포트 자동화 시스템</b><br>
  <span style="color:#56B16F">위해물품 실시간 탐지 · 통계 시각화 · LLM 기반 보안 브리핑 리포트</span>
</p>

<br>

## ✨ 프로젝트 소개 (About The Project)
> [주의사항] <br>
> **SafeGate** 시스템은 메인 비즈니스 로직을 담당하는 **Spring Boot 서버**와 AI 연산 및 PDF 생성을 전담하는 **Django 서버**로 분리되어 있습니다. 정상적인 동작을 위해 두 서버가 모두 실행되어야 합니다.

<br>

<b>YOLO(You Only Look Once)</b> 기반의 객체 탐지 모델을 활용하여 X-ray 수하물 이미지 내의 위해물품(총기, 도검, 액체류 등 38종)을 실시간으로 판독합니다. 또한 탐지 이력(데이터)을 바탕으로 <b>LLM(OpenAI)</b>이 주간/월간 보안 동향을 분석하고, 시각화된 그래프가 포함된 <b>PDF 리포트를 자동 생성</b>하여 관리자에게 제공하는 스마트 공항/건물 보안 솔루션입니다.

## 🚀 주요 기능 (Key Features)

- 🤖 **AI X-ray 정밀 탐지**: 클라이언트가 업로드한 이미지를 Django AI 서버로 전송하여 YOLO 모델로 분석 후 Bounding Box가 그려진 결과 및 신뢰도(%) 반환.
- 📊 **LLM 기반 지능형 리포트**: 탐지 통계(Pandas 처리)를 바탕으로 GPT 모델이 요약 코멘트를 작성하고, Matplotlib 차트와 함께 A4 사이즈의 PDF 보고서(ReportLab) 자동 생성.
- 🔐 **보안 및 인증 시스템**: JWT(Access/Refresh) 기반 자체 로그인 및 **카카오 OAuth 2.0** 소셜 로그인 지원, 토큰 블랙리스트(로그아웃) 관리.
- 🚨 **실시간 경고 알림**: 위해물품 탐지 시 시스템 내 이력 저장 및 `JavaMailSender`를 활용한 관리자 이메일 자동 발송.
- 📁 **탐지 이력 게시판**: 기간별 유해물품 탐지 기록 페이징 조회 및 상세 결과(이미지, 탐지 항목) 조회 기능.

<br>

## 📁 폴더 구조 (Folder structure)
```bash
SafeGate/
├── BE/Spring/safe-guard/        # [Spring Boot] 메인 백엔드 서버
│   ├── src/main/java/dna/safe_guard/
│   │   ├── config/              # RestTemplate, WebMvc 설정
│   │   ├── controller/          # REST API (X-ray, Board, Report, User 등)
│   │   ├── dto/                 # Request/Response DTO 
│   │   ├── entity/              # JPA 엔티티 (User, Event, Detection 등)
│   │   ├── repository/          # DB 접근 레포지토리
│   │   ├── security/            # JWT 필터 및 Spring Security 설정
│   │   └── service/             # 비즈니스 로직 (AI 연동, 인증, 메일 등)
│   ├── src/main/resources/      # application.properties 등 환경 설정
│   ├── build.gradle             # Spring 빌드 설정 파일
│   └── .env                     # Spring DB 및 API 키 환경 변수 (미제공)
│
└── AI/Django/                   # [Django] AI & PDF 마이크로서비스
    ├── ai/                      # X-ray 이미지 객체 탐지 (YOLOv8) 앱
    ├── config/                  # Django 메인 설정 (CORS, URLs 등)
    ├── fonts/                   # PDF 생성용 폰트 파일 (NanumGothic)
    ├── media/                   # 업로드 이미지 및 생성된 리포트/그래프 저장
    ├── models/                  # YOLOv8 모델 파일 (.pt)
    ├── pdf/                     # 데이터 가공, OpenAI 분석, PDF 생성 앱
    ├── manage.py                # Django 실행 스크립트
    ├── requirements.txt         # Python 패키지 의존성 파일
    └── .env                     # Django API 키 및 환경 변수 (미제공)
```

<br>

## 🖥️ 실행 환경 및 요구사항 (Run Environment)

- **Java Backend:** Java 17+, Spring Boot 3.x
- **Python Backend:** Python 3.9+, Django
- **Database:** MySQL 8.0+
- **AI / Data Libraries:** Ultralytics (YOLO), OpenAI, Pandas, Matplotlib, ReportLab
- **External API:** Kakao Developers (REST API Key), OpenAI API Key


## ✅ 지원 서비스 (Supported Services)

### Spring Boot (Main API)
* **Image Hosting:** 프론트엔드에서 접근할 수 있도록 로컬 스토리지에 결과 이미지 서빙 (`/images/**`).
* **DB Tracking:** 탐지된 물품별(Type 0~37) 카운팅 및 이벤트 추적, 통계 리포트 다운로드 URL 이력 관리.
* **OAuth & JWT:** 카카오 인가 코드를 받아 자체적으로 JWT 세션을 발급하는 통합 인증 체계.

### Django (AI & Report Engine)
* **Computer Vision:** `ultralytics`를 사용해 X-ray 원본 이미지에 대한 고속 인퍼런스 수행 후 Base64로 인코딩하여 Spring에 반환.
* **Data Visualization & PDF:** Spring으로부터 받은 JSON 로그 데이터를 Pandas 데이터프레임으로 변환해 원형/꺾은선 그래프 생성 및 텍스트 기반 브리핑 PDF 합성.

---

<br>

## ⚡️ 설치 및 실행 (Quick Start)

### 1. 환경 설정 (Environment Setup)

#### [Spring Boot 설정] `src/main/resources/application.properties`
```properties
# DB 설정
spring.datasource.url=jdbc:mysql://localhost:3306/safegate_db
spring.datasource.username=<DB_USERNAME>
spring.datasource.password=<DB_PASSWORD>

# 파일 업로드 경로
file.upload.directory=C:/safegate-images/

# 카카오 OAuth & 이메일 알림 설정
kakao.client-id=<KAKAO_REST_API_KEY>
kakao.redirect-uri=<REDIRECT_URI>
spring.mail.username=<SENDER_EMAIL>
spring.mail.password=<APP_PASSWORD>

# Django 통신 URL
django.api.url=http://localhost:8000/api/ai/detect/
django.report.url=http://localhost:8000/api/pdf/report/
```

#### [Django 설정] 프로젝트 루트 `.env`
```env
DEBUG=True
SECRET_KEY=<DJANGO_SECRET_KEY>
OPENAI_API_KEY=<YOUR_OPENAI_API_KEY>
YOLO_MODEL_PATH=./models/best.pt
SPRING_SERVER_URL=http://localhost:8080
CORS_ALLOW_ALL=True
```

### 2. 서버 실행 (Run Servers)

**터미널 1: Django 서버 실행 (포트 8000)**
```bash
cd AI/Django

# 가상환경 생성 및 활성화 (Windows 기준)
python -m venv venv
./venv/Scripts/activate

# 패키지 설치
pip install -r requirements.txt

# uvicorn을 이용한 ASGI 서버 실행
python -m uvicorn config.asgi:application --reload --host 127.0.0.1 --port 8000
```
(※ Mac/Linux 환경의 경우 가상환경 활성화 시 source venv/bin/activate 명령어를 사용해주세요.)


**터미널 2: Spring Boot 서버 실행 (포트 8080)**
```bash
cd spring-server
./gradlew bootRun
```

<br>

## 💡 **API 사용 예시 (Usage Example)**

<details>
<summary><b>📷[1. X-ray 이미지 탐지 요청] (AI Detection)</b></summary>

> 클라이언트가 이미지를 보내면, Spring이 Django에 분석을 위임하고 최종 결과를 DB에 저장한 뒤 반환합니다.

```bash
curl -X POST http://localhost:8080/x-ray/image-upload \
     -H "Authorization: Bearer <ACCESS_TOKEN>" \
     -F "image=@luggage_xray.jpg;type=image/jpeg"
```

**응답 예시:**
```json
{
    "image_file": "http://localhost:8080/images/luggage_xray1.jpg",
    "total_detected": 2,
    "detections":[
        { "item": "knife", "probability_percent": 95.5 },
        { "item": "lighter", "probability_percent": 88.2 }
    ]
}
```
</details>

<br>

<details>
<summary><b>📄 [2. 주간/월간 보안 리포트 생성] (PDF Generation)</b></summary>

> 기간 내 DB에 누적된 탐지 데이터를 취합하여 Django 측에 전송 후, 완성된 PDF 파일을 바이너리(다운로드) 형태로 응답받습니다.

```bash
curl -X GET "http://localhost:8080/report?period=week" \
     -H "Authorization: Bearer <ACCESS_TOKEN>" \
     --output SafeGate_week_report.pdf
```
> **Response:** 브라우저에서 요청 시 첨부파일(PDF) 형태로 즉시 다운로드 됩니다.
</details>

<br>

<details>
<summary><b>📋 [3. 탐지 이력 게시판 조회] (Board/History)</b></summary>

**최근 일주일 데이터 시각화용 데이터 조회**
```bash
curl -X GET http://localhost:8080/board/events/recent \
     -H "Authorization: Bearer <ACCESS_TOKEN>"
```

**특정 탐지 이벤트 상세 조회**
```bash
curl -X GET http://localhost:8080/board/detail/15 \
     -H "Authorization: Bearer <ACCESS_TOKEN>"
```
**응답 예시:**
```json
{
    "src": "http://localhost:8080/images/202603011130001.png",
    "start-time": "2026-03-01 11:30:00",
    "detect": [16, 18] 
}
```
*(※ detect 배열의 번호는 도검류, 폭발물 등 지정된 타입 인덱스를 의미합니다)*
</details>

<br>

<details>
<summary><b>🔑[4. 인증 (카카오 로그인)] (Authentication)</b></summary>

> 프론트엔드에서 발급받은 카카오 인가(Authorization) 코드를 서버로 전송하여 자체 JWT를 발급받습니다.

```bash
curl -X POST http://localhost:8080/user/kakao-login \
     -H "Content-Type: application/json" \
     -d '{
           "code": "kHjF...카카오_인가_코드...1xSw"
         }'
```
</details>

---

## ✍️ 작성자 (Author)

이 프로젝트는 **[SafeGate]** 지능형 공항/수하물 보안 솔루션의 백엔드 통합 API 서버입니다.

* **Team:** SafeGate
* **Members:**
  * 홍준기 ([@JunGi-Hong](https://github.com/JunGi-Hong))
  * 금제윤 ([@jaeyoon882](https://github.com/jaeyoon882))
  * Naddong ([@Naddong](https://github.com/Naddong))
  * onestar0116-create ([@onestar0116-create](https://github.com/onestar0116-create))
* **Stack:** Spring Boot, Django, YOLOv8, OpenAI API, MySQL