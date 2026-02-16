# MediView

비대면 진료 중계 플랫폼 — 환자와 의료기관(의사)을 안전하게 연결하는 텔레헬스 서비스

## Tech Stack

| 구분 | 기술 |
|------|------|
| Backend | Spring Boot 4.0.2, Java 21 |
| Database | MariaDB, JPA/Hibernate |
| Security | Spring Security, JWT (HS256) |
| Realtime | WebSocket (채팅), WebRTC Signaling (화상) |
| AI Orchestrator | Python, FastAPI, LangGraph, LangChain |
| RAG | ChromaDB, OpenAI Embeddings |
| API Docs | Swagger/SpringDoc OpenAPI 3.0 |
| Build | Gradle 9.3 |

## Project Structure

```
MediView/
├── src/main/java/com/mediview/
│   ├── domain/
│   │   ├── ai/                  # AI Job 관리
│   │   ├── appointment/         # 예약/접수
│   │   ├── audit/               # BaseEntity, BaseTimeEntity
│   │   ├── auditlog/            # 감사로그, 접근로그, 분기리포트
│   │   ├── consultation/        # 진료 세션, 채팅, 의무기록, WebSocket
│   │   ├── document/            # 문서/서류 발급
│   │   ├── enums/               # 열거형 (17개)
│   │   ├── exception/           # 예외 처리
│   │   ├── intake/              # 문진 (증상입력)
│   │   ├── notification/        # 알림
│   │   ├── organization/        # 의료기관, 의사 프로필
│   │   ├── payment/             # 결제, 환불, 정산
│   │   ├── policy/              # 정책 관리, 정책위반
│   │   ├── response/            # ApiResponse 래퍼
│   │   └── user/                # 사용자, RBAC, KYC
│   └── global/
│       ├── config/              # Swagger, WebSocket, ModelMapper, CORS
│       └── security/            # JWT, AuthFilter, SecurityConfig
├── ai-orchestrator/             # Python AI 서비스
│   ├── app/
│   │   ├── graphs/              # LangGraph 워크플로우 (3개)
│   │   ├── nodes/               # 그래프 노드 (PII, 구조화, 위험신호 등)
│   │   ├── rag/                 # ChromaDB RAG 파이프라인
│   │   ├── schemas/             # Pydantic 스키마
│   │   └── main.py              # FastAPI 엔트리포인트
│   ├── requirements.txt
│   └── Dockerfile
└── build.gradle
```

## Getting Started

### Prerequisites

- Java 21+
- MariaDB
- Python 3.12+ (AI Orchestrator)
- OpenAI API Key (AI 기능 사용 시)

### Backend (Spring Boot)

```bash
# 1. DB 설정 — src/main/resources/secret.properties 생성
echo "spring.datasource.url=jdbc:mariadb://localhost:3306/mediview" > src/main/resources/secret.properties
echo "spring.datasource.username=root" >> src/main/resources/secret.properties
echo "spring.datasource.password=your_password" >> src/main/resources/secret.properties

# 2. 빌드 및 실행
./gradlew build
./gradlew bootRun
```

서버 기동 후:
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui`

### AI Orchestrator (Python)

```bash
cd ai-orchestrator

# 1. 가상환경 및 의존성
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# 2. 환경변수
cp .env.example .env
# .env에 OPENAI_API_KEY 설정

# 3. 실행
uvicorn app.main:app --host 0.0.0.0 --port 8001
```

AI API: `http://localhost:8001/docs`

## API Reference

### Auth (인증) — 공개

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/auth/signup` | 회원가입 |
| POST | `/api/auth/login` | 로그인 (JWT 발급) |

### KYC (본인확인) — 인증 필요

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/kyc/request` | 본인확인 요청 |
| POST | `/api/kyc/verify` | 인증번호 검증 |

### User (사용자) — 인증 필요

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/users/me` | 내 프로필 조회 |
| PUT | `/api/users/me` | 내 프로필 수정 |

### Doctor (의사) — 인증 필요

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/doctors/me` | 내 의사 프로필 조회 |
| PUT | `/api/doctors/me` | 내 의사 프로필 수정 |

### Appointment (예약/접수) — 인증 필요

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/appointments` | 예약/즉시접수 생성 |
| GET | `/api/appointments?status=` | 예약 목록 조회 |
| GET | `/api/appointments/{id}` | 예약 상세 조회 |

### Intake (문진) — 인증 필요

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/appointments/{id}/intakes` | 문진 제출 |
| GET | `/api/appointments/{id}/intakes` | 문진 목록 조회 |

### Consultation (진료 세션) — 인증 필요

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/sessions` | 진료 세션 생성 |
| GET | `/api/sessions/{id}` | 세션 상세 (webrtcRoomId 포함) |
| POST | `/api/sessions/{id}/end` | 진료 종료 |

### Document (문서/서류) — 인증 필요

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/documents` | 문서 생성 (DRAFT) |
| PUT | `/api/documents/{id}` | 문서 내용 수정 |
| POST | `/api/documents/{id}/approve` | 승인 → ISSUED |
| GET | `/api/documents/{id}/download` | 문서 다운로드 |

### Payment (결제) — 인증 필요

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/payments/prepare` | 결제 준비 |
| POST | `/api/payments/confirm` | 결제 확인 |
| POST | `/api/refunds` | 환불 요청 |

### Notification (알림) — 인증 필요

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/notifications` | 내 알림 목록 |
| PUT | `/api/notifications/{id}/read` | 읽음 처리 |

### AI Job (AI 작업) — 인증 필요

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/ai/intake-summary` | 문진 요약 AI 작업 생성 |
| POST | `/api/ai/visit-summary` | 진료 요약 AI 작업 생성 |
| GET | `/api/ai/jobs/{id}` | AI 작업 상태 조회 |

### Admin — Organization (관리자) — ADMIN 권한

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/admin/organizations` | 의료기관 등록 |
| GET | `/api/admin/organizations` | 의료기관 목록 |
| POST | `/api/admin/organizations/{id}/doctors/invite` | 의사 초대 |

### Admin — Policy (관리자) — ADMIN 권한

| Method | Endpoint | 설명 |
|--------|----------|------|
| PUT | `/api/admin/policies/{key}` | 정책 생성/수정 |
| GET | `/api/admin/policies` | 정책 목록 조회 |

### Admin — Settlement (관리자) — ADMIN 권한

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/admin/settlements/generate` | 기간별 정산 생성 |
| GET | `/api/admin/settlements?period=` | 정산 조회 |

### Admin — Audit/Report (관리자) — ADMIN 권한

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/admin/audit-logs` | 감사로그 조회 |
| POST | `/api/admin/reports/quarterly` | 분기 리포트 생성 |
| GET | `/api/admin/reports/quarterly` | 분기 리포트 목록 |

### WebSocket (실시간)

| Protocol | Endpoint | 설명 |
|----------|----------|------|
| WS | `/ws/chat/{sessionId}` | 진료 채팅 (메시지 DB 저장) |
| WS | `/ws/webrtc/{roomId}` | WebRTC 시그널링 (offer/answer/ICE) |

### AI Orchestrator (Python — port 8001)

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/health` | 헬스체크 |
| POST | `/api/ai/intake-summary` | LangGraph 문진 정리 |
| POST | `/api/ai/visit-summary` | LangGraph 진료 후 요약 |
| POST | `/api/ai/cs-answer` | 정책 RAG CS 답변 |
| POST | `/api/rag/ingest` | RAG 문서 수집 |
| GET | `/api/ai/jobs/{job_id}` | AI 작업 상태 |

## Architecture

### Core Principles

1. **의료행위 비개입** — 플랫폼은 진단/처방 추천/의사결정 대체를 하지 않음
2. **AI 출력 = DRAFT** — 모든 AI 생성물은 DRAFT 상태로 저장, 의료인 승인 후에만 발행
3. **추천/유도 금지** — 특정 의료기관/약국 추천, 금전적 이익 제공/수취 금지를 시스템으로 강제
4. **감사 추적** — 모든 주요 변경에 대한 불변 감사로그 기록
5. **안전 필터** — PII 마스킹, 금지표현 필터, "의료판단 아님" 고지 자동삽입

### LangGraph Workflows

```
Graph A: 문진 정리 (Intake Summary)
  PII 마스킹 → 구조화 추출 → 위험신호 감지 → 질문 제안 → 의사용 포맷

Graph B: 진료 후 요약 (Visit Summary)
  쉬운말 변환 → 안전필터

Graph C: CS 정책 RAG (CS Answer)
  쿼리 분류 → 정책문서 검색 → 답변 생성 → 안전필터
```

### ERD Overview (27 Tables)

**계정/권한**: users, user_profiles, organizations, doctor_profiles, rbac_roles, rbac_permissions, rbac_user_roles, kyc_verifications

**진료**: appointments, intake_forms, intake_files, consult_sessions, consult_messages, medical_records

**결제/정산**: payments, refunds, settlements

**문서**: documents, document_files

**정책/감사**: policies, policy_violations, audit_logs, access_logs, quarterly_reports

**AI/RAG**: ai_jobs, ai_outputs, rag_documents, rag_chunks

**알림**: notifications

## Security

| 구분 | 설정 |
|------|------|
| 인증 | JWT Bearer Token (HS256) |
| 비밀번호 | BCrypt |
| 세션 | Stateless |
| CORS | localhost, 127.0.0.1, 지정 IP |
| 권한 | `/api/admin/**` → ADMIN only |
| 공개 | `/api/auth/**`, `/swagger-ui/**`, `/ws/**` |
| KYC | CI/DI 해시 기반 본인확인 |
