# 🏠 GoZip - 부동산 거래 정보 플랫폼

600만개의 거래정보 데이터를 활용하여 다양한 정보를 제공받을 수 있습니다. 

Spring Boot와 Vue.js 기반의 부동산 지도 서비스 프로젝트입니다.

## 📌 프로젝트 개요

- **프로젝트명**: GoZip (고집)
- **기간**: 2025.05.21 ~ 2025.05.28
- **팀원**: [이름1], [이름2], ...
- **주요기능**: 지역 기반 아파트 정보 제공, AI 추천, 사용자 커뮤니티
### 🙋‍♂️ Team members

[<img src="https://avatars.githubusercontent.com/u/129932517?v=4" width="150px">](https://github.com/Jihye511)|[<img src="https://avatars.githubusercontent.com/u/129295064?v=4" width="150px;" alt=""/>](https://github.com/s27970) |
|:---:|:---:|
|[이지혜](https://github.com/Jihye511) |[손영재](https://github.com/oz115) |
|카카오 지도 연결</br> ai 추천 및 지도 연동 </br>  건물 시세 시각화 </br>  커뮤니티  | 👑 **PM** </br> 회원정보 </br> 지도 내 범위 검색 </br> 건물별 리뷰 및 평점 </br>  계산기 |
---

## 🚀 주요 기능

### ✅ 아파트 정보 탐색
- 시/군/구, 동 단위 검색
- 지도 기반 아파트 마커 및 정보 표시
- 아파트별, 평수별 시세 그래프 제공
- 실거주자 리뷰 제공
<img width="1470" alt="image" src="https://github.com/user-attachments/assets/b41310b1-60f8-48ad-a066-5e1c4e7b37e2" />


### 🧠 AI 아파트 추천
- GPT 기반 조건형 추천 (체크리스트 제공)
- 결과 아파트 지도에 표시하여 더 자세한 정보 제공
  
<img width="331" alt="image" src="https://github.com/user-attachments/assets/4aa76fb6-91d9-483b-a549-25eb06604aa0" />

### 📍범위 검색을 통한 시세 증감률 랭킹
- 사용자 지정 지역 범위, 비교 년도 기준 시세 증감률 랭킹 제공
  

### 💬 커뮤니티 기능
- 게시글/댓글 작성 및 조회
- 사용자 간 정보 공유 가능

---

## 🛠 사용 기술

### 🔧 Backend
- **Framework**: Spring Boot
- **Database**: MySQL
- **AI 연동**: Spring AI, OpenAI GPT
- **보안**: Spring Security

### 💻 Frontend
- **Framework**: Vue.js 3 + Vite
- **지도 API**: Kakao Map API
- **상태관리**: Pinia
- **스타일링**: Tailwind CSS





### 요구사항명세서
https://docs.google.com/spreadsheets/d/1zrRtcDrmhplDt20apE999OgHi7lIRaEqgyQfeq4jJh8/edit?gid=0#gid=0

![image.png](./요구사항명세서.png)


### ERD
![image.png](./image.png)


### classdiagram
![classdiagram.png](./classdiagram.png)
