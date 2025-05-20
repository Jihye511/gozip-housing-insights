# 📄 SSAFY_HOME 프로젝트 REST 적용

## 팀구성성

**팀장:** 손영재  
**팀원:** 오현석, 윤진

---

# UserRestController API 문서

**기본 URL 접두어:** `/api/user`

| 메서드   | 엔드포인트                | 설명                                                      | 요청 파라미터                                       | 응답                                                                 |
| -------- | ------------------------- | --------------------------------------------------------- | ------------------------------------------------- | -------------------------------------------------------------------- |
| **GET**  | `/api/user/mypage`        | 로그인한 사용자의 정보를 조회합니다.                      | 세션에 `loginUser`가 존재해야 합니다.             | 성공: 사용자 정보 (200 OK) <br> 실패: `"message": "Authentication required."` (401) |
| **PUT**  | `/api/user/update`        | 사용자의 정보를 수정합니다.                               | `UserDto` (수정할 사용자 정보)                    | 성공: `"message": "User information updated successfully."` (200 OK) <br> 실패: `"error": "Failed to update user information."` (500) |
| **DELETE** | `/api/user/delete`       | 사용자를 삭제합니다.                                      | 세션에 `loginUser`가 존재해야 합니다.             | 성공: `"message": "User deleted successfully."` (200 OK) <br> 실패: `"error": "Failed to delete user."` (500) |
| **POST** | `/api/user/signup`        | 새로운 사용자를 등록합니다.                              | `UserDto` (사용자 정보)                            | 성공: `"message": "User registered successfully."` (201 Created) <br> 실패: `"error": "User ID already exists."` 또는 `"error": "Email already exists."` (400) |

---

## 요청

### 1. **GET `/api/user/mypage`**
- **설명:** 로그인한 사용자의 정보를 조회합니다.
- **요청 파라미터:** `없음` (세션에 `loginUser`가 존재해야 합니다.)


### 2. **PUT `/api/user/update`**
- **설명:** 사용자의 정보를 수정합니다.
- **요청 파라미터:**
  - `UserDto`: 수정할 사용자 정보


### 3. **DELETE `/api/user/delete`**
- **설명:** 로그인한 사용자를 삭제합니다.
- **요청 파라미터:** `없음` (세션에 `loginUser`가 존재해야 합니다.)

### 4. **POST `/api/user/signup`**
- **설명:** 새로운 사용자를 등록합니다.
- **요청 파라미터:**
  - `UserDto`: 사용자 정보

---

# AptRestController API 문서

**기본 URL 접두어:** `/api/apt`

| 메서드   | 엔드포인트                | 설명                                                      | 요청 파라미터                                       | 응답                                                                 |
| -------- | ------------------------- | --------------------------------------------------------- | ------------------------------------------------- | -------------------------------------------------------------------- |
| **GET**  | `/api/apt/{aptSeq}/deals`  | 아파트 거래 정보를 조회합니다.                             | `aptSeq` (아파트 시퀀스)                           | 성공: 거래 목록 (200 OK) <br> 실패: `"message": "No deals found for this apartment sequence."` (404) |
| **GET**  | `/api/apt/search`          | 아파트 이름 또는 동 코드로 아파트를 검색합니다.            | `aptName` (아파트 이름, 선택사항) <br> `dongCode` (동 코드, 선택사항) | 성공: 아파트 목록 (200 OK) <br> 실패: `"error": "Please provide aptName or dongCode for search."` (400) |

---

## 요청

### 1. **GET `/api/apt/{aptSeq}/deals`**
- **설명:** 특정 아파트 시퀀스를 기반으로 거래 목록을 조회합니다.
- **요청 파라미터:**
  - `aptSeq`: 아파트 시퀀스

### 2. **GET `/api/apt/search`**
- **설명:** 아파트 이름(`aptName`) 또는 동 코드(`dongCode`)로 아파트를 검색합니다.
- **요청 파라미터:**
  - `aptName`: 아파트 이름 (선택사항)
  - `dongCode`: 동 코드 (선택사항)

---




