# DevTallks 백엔드 정리

## 개요
- Java 17 / Spring Boot 3.5
- 주요 기술: Spring Web, Spring Security + JWT, Spring Data JPA, Redis, WebSocket(STOMP), Validation, SpringDoc
- DB: MySQL, 캐시/세션: Redis

## 공통/인프라
- JWT: Access/Refresh 발급, `JwtAuthenticationFilter`로 인증, `AccessTokenBlacklistService`로 로그아웃 시 액세스 토큰 블랙리스트 처리, Refresh 토큰은 Redis에 저장(`RefreshTokenService`).
- Security: Stateless, `/auth/**`, `/ws/**`, Swagger 경로 허용, 나머지 인증 필요. CORS 허용 패턴(`*`) 기본 적용.
- 공통 응답/예외: `ResponseDto`, `GlobalExceptionHandler` (검증 오류, IllegalArgumentException, AccessDenied 등).
- Redis: `RedisConfig` (Lettuce), 문자열/Long 템플릿 빈 제공.
- WebSocket: STOMP 엔드포인트 `/ws`, 브로커 `/topic`, 앱 프리픽스 `/app`.

## 도메인별 구현
### 회원/Auth
- 회원가입/로그인/리프레시/로그아웃: `AuthController`, `MemberService`.
- 패스워드 Bcrypt 저장, 이메일/닉네임 중복 검사.
- 로그아웃 시 Refresh 삭제 + Access 토큰 블랙리스트.

### 팔로우
- 팔로우/언팔로우/목록 조회(`FollowController`, `FollowService`).
- 응답 DTO 래핑(`ResponseDto`).

### 프로필
- 프로필 조회/수정/이미지 수정 시 소유자 확인(`ProfileController`, `ProfileService`).

### 게시글(Article)
- CRUD + 댓글, 좋아요 카운트, 조회수/인기글 캐싱(`ArticleService`).
- 조회 시 Redis로 조회수 증가, 인기글 ZSET 유지.
- 스케줄러가 5분(기본)마다 Redis 뷰 델타를 DB 반영하고 인기글 리스트를 브로드캐스트.
- 실시간: 조회수 업데이트 `/topic/articles/{id}/views`, 인기글 `/topic/articles/popular`.

### 피드(Feed)
- CRUD, 좋아요, 댓글 CRUD(`FeedService`, `FeedCommentService`).
- 권한 검증: 작성자/댓글 작성자만 수정·삭제.
- 실시간 이벤트(STOMP):
  - 피드 생성/수정/삭제: `/topic/feeds`, `/topic/feeds/{id}`
  - 좋아요 변화: `/topic/feeds/{id}/likes`
  - 댓글 생성/수정/삭제: `/topic/feeds/{id}/comments`

### 댓글
- Article/Feed 댓글 각각 서비스/컨트롤러, 작성자 검증 후 수정·삭제 허용.
- 요청 DTO에 Validation 적용.

### 조회수/인기글 설정값
- `application.properties`에 기본값 주석:
  - `article.view-cache.view-ttl` (기본 24h)
  - `article.view-cache.popular-ttl` (기본 24h)
  - `article.view-cache.popular-trim-limit` (기본 500)
  - `article.view-cache.flush-interval` (기본 5m)

## 권한/검증 핵심
- 모든 수정/삭제는 인증 사용자와 리소스 소유자 비교 후 처리.
- DTO에 `@Valid` 적용(주요 요청 DTO).
- 인증 사용자(`CustomUserDetails`)를 컨트롤러에서 주입해 서비스로 전달.

## 테스트/운영 주의
- `spring.jpa.open-in-view` 기본 WARN → 필요시 `false`로 설정 권장.
- Redis/DB가 동작해야 조회수/인기글/리프레시/블랙리스트 로직 정상 작동.
- 아직 자동 테스트 미작성 → `./gradlew test`/통합 테스트 추가 필요.
