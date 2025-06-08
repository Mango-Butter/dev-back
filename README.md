## 🥭 망고보스 : 알바생 관리 자동화 솔루션

> www.mangoboss.store


## ⚙️ 시스템 개요
<img width="600" alt="KakaoTalk_Photo_2025-06-08-21-21-47" src="https://github.com/user-attachments/assets/c8d067cd-419e-4e6e-8d98-ac9e3721169b" />
<br>
<br>

## ERD 다이어그램
<img src="https://github.com/user-attachments/assets/ac504d7f-e1c6-4662-ad5b-a312ea2f9067" width="600"/>
<br>
<br>

## 멀티 모듈 모노 레포 구조
<img width="600" alt="KakaoTalk_Photo_2025-06-08-21-40-18" src="https://github.com/user-attachments/assets/5967b4b1-234c-4318-9fef-99dc9295f6d8" />
<br>
하나의 레포지토리에서 app, admin, batch 서버를 구성하는 멀티 모듈 모노 레포 구조입니다.
각 모듈은 독립적으로 개발 및 배포가 가능하며, 공통 모듈을 통해 코드 중복을 최소화합니다.
<br>
<br>

## 트랜잭션 아웃박스 패턴
<img width="600" alt="KakaoTalk_Photo_2025-06-08-21-21-54" src="https://github.com/user-attachments/assets/4f590add-ec12-4f93-8383-e99c882c4656" />
<br><br>

트랜잭션 아웃박스 패턴을 사용하여, 이벤트 발행과 데이터베이스 트랜잭션을 일관되게 처리합니다. <br>
이벤트를 소모하는 서비스는 이벤트를 발행하는 서비스와 독립적으로 동작할 수 있으며, 재시도 및 장애 복구가 용이합니다.<br>
또한 이벤트를 소모하는 서비스에서 Rate Limiting을 적용하여, 이벤트 처리 속도를 조절하여 안정적인 시스템 운영이 가능합니다.
<br>
<br>

## 신뢰성 있는 근로계약서 시스템
<img width="600" alt="KakaoTalk_Photo_2025-06-08-21-21-51" src="https://github.com/user-attachments/assets/f320e245-b4b1-4120-b99d-2901f85dab15" />

<br><br>
<ol>
  <li>SSE-KMS 방식으로 계약서 PDF를 암호화하고, S3에 저장합니다.</li>
  <li>CloudTrail로 KMS 키 사용 및 접근 로그를 기록합니다.</li>
  <li>완성된 계약서 PDF의 SHA-256 해시 값을 저장하여, 계약서의 무결성을 검증할 수 있습니다.</li>
  <li>Presigned URL 기반 제한 시간 내 접근 허용으로 계약서의 보안을 강화합니다.</li>
</ol>
