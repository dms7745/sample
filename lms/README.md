# 📖 입트영(입이 트이는 영어) 스마트 학습 플랫폼 (LMS)
본 프로젝트는 학습 효율을 극대화하기 위해 설계된 맞춤형 교육 관리 시스템입니다.<br>
수강생의 학습 여정을 데이터로 관리하여 체계적인 영어 습득 환경을 제공합니다.

## URL https://lms.da2un.store

## 1. 프로젝트 개요
- 개발 기간: 2025.10.15 ~ 2025.12.31<br>
- 주요 목적: 영어 학습 콘텐츠의 디지털화 및 개인 맞춤형 학습 관리 환경 구축<br>
- 활용 방안 및 기대 효과 : 맞춤형 강의로 인한 실력 향상, 다양한 연령의 고객 이용 가능, 레벨 테스트를 통한 맞춤 강의 제공

## 2. 주요 기능
- 회원 관리: 회원가입, 로그인, 마이페이지를 통한 학습 이력 관리<br>
- 강의 시스템: 동영상 강의 제공<br>
- 게시판 운영: 공지사항 및 Q&A 게시판을 통한 커뮤니티 기능<br>
- 학습 대시보드: 개인별 학습 진도율 시각화 및 관리 기능<br>

## 3. 주요 페이지 소개
1. 회원가입<br>
   강사, 수강생으로 회원 가입 가능 (강사는 관리자가 권한 부여 시 계정 사용 가능)
   
   <img width="1106" height="910" alt="image" src="https://github.com/user-attachments/assets/88ac8e8b-da40-4a2f-9270-6eefac4252b5" />



2. 로그인
   
   <img width="694" height="354" alt="image" src="https://github.com/user-attachments/assets/9a6e5f26-dca7-42bd-a7ea-e8a5bcdfad2e" />

  - 회원 첫 로그인 시 초기 테스트 (20문항)
   
    <img width="1350" height="864" alt="image" src="https://github.com/user-attachments/assets/ec2ce44f-9a3a-45f6-b5cc-88660822bb87" />

  - 테스트 결과
     80점 이상 고급, 60점 이상 중급, 그 아래는 초급 등급의 강의까지만 수강신청 가능<br>
     레벨테스트 결과가 초급이 나왔으므로 중급, 고급 강의는 리스트에 뜨지 않음.
   
    <img width="1333" height="892" alt="image" src="https://github.com/user-attachments/assets/2b79aa78-de48-454b-b209-ab3e73132f6c" />



3. 강의 보기
   수강신청 후 영상 10분이상 시청 시 수강완료 처리가능 (현재 20초 설정)<br>
   10분 미만 시청 시 10분 이상 시청 팝업 알림

   <img width="1337" height="829" alt="image" src="https://github.com/user-attachments/assets/dd1fa983-553e-4d25-b693-317913d76b06" />
   <img width="774" height="344" alt="image" src="https://github.com/user-attachments/assets/5368a17e-7026-45b9-b42e-0cd2fc6ee64e" />


4. 등급업 테스트
   본인 등급 영상10개 이상 수강완료 처리 시 등급업 테스트 출력<br>
   80점 이상이면 초급일 시 중급으로 승급 및 초, 중급 강의 수강 가능 고급은 등급업테스트 없이 “최고등급입니다”로 표기

   <img width="672" height="280" alt="image" src="https://github.com/user-attachments/assets/bbbc7de0-b0a9-4e1b-bf1b-9ea9f1f16954" />
   <img width="1328" height="756" alt="image" src="https://github.com/user-attachments/assets/b01267ed-62c7-44dd-9d50-8795881dfd68" />


5. 게시판
   
   <img width="856" height="312" alt="image" src="https://github.com/user-attachments/assets/21dc200e-03f0-43b1-85e1-9cb1d00b15a1" />


6. 강의  목록(수강생)
   
   <img width="1342" height="897" alt="image" src="https://github.com/user-attachments/assets/adb9f033-47ce-48e3-8c9c-acaf8a3361f3" /><br>
   
7. 마이페이지(수강생/강사/관리자)<br>
  - 수강생<br>
    신청강좌 / 완료강좌 / 내 질문 / 내 답글 / 내 정보 수정<br>
   
    <img width="704" height="348" alt="image" src="https://github.com/user-attachments/assets/04b3bf5f-98d7-4614-bf9f-4105f9607604" />

 - 강사<br>
   내 강의 목록 / 내 강의 관련 질문 / 강의 등록 / 테스트 관리 / 수강생 관리<br>
   
   <img width="704" height="330" alt="image" src="https://github.com/user-attachments/assets/996f9e73-3d5b-481c-ba83-7fd46290355e" />

 - 관리자<br>
   사용자 목록 / 비승인 강사 목록 / 휴면 회원 관리<br>
   
   <img width="1381" height="736" alt="image" src="https://github.com/user-attachments/assets/1bde1375-2290-4e61-bb68-926e9af5eae2" />


## 4. 데이터베이스 설계 (ERD)
사용자(User), 강의(Course), 게시글(Post), 학습기록(Progress) 간의 관계를 고려한 정규화된 DB 설계 적용

<img width="2501" height="1312" alt="image" src="https://github.com/user-attachments/assets/2deb42c4-e370-4bd5-b6e1-98817b3cfa11" />

## 5. 기술 스택 (Tech Stack)
- Backend<br>
   Java (Spring 기반)
  
- Frontend<br>
   Languages/Tools: HTML5, CSS3, JavaScript<br> 
   Template Engine: Thymeleaf<br>

- Database<br>
   H2 Console

- Collaboration<br>
   Notion

- Development Tool<br>
   STS4 (Spring Tool Suite 4)







