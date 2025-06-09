# Github Actions and AWS CI/CD 실습

---

## 📌 프로젝트 개요

- Spring Boot 3.4.6
- Gradle 빌드
- MariaDB 연동
- 파일 업로드 → S3 저장
- Github Actions CI/CD 적용
- EC2 배포

---

## 📋 실습 흐름 요약

1️⃣ AWS 자원 준비 (EC2, S3, IAM 등)  
2️⃣ Github 저장소 생성 및 본 프로젝트 업로드  
3️⃣ Github Actions Secrets 설정  
4️⃣ 배포 테스트  

---

## 🚀 Github Actions Secrets 등록 

| Key                     | Value                  |
|-------------------------|------------------------|
| EC2_HOST                | IPv4                   |
| EC2_USERNAME            | ubuntu                 |
| EC2_PRIVATE_KEY         | Key pair               |
| APPLICATION_PROPERTIES  | application.properties |

---

## 🚀 AWS IAM 권한 

- AmazonS3FullAccess
- AmazonEC2FullAccess
- AmazonEC2InstanceConnectFullAccess
- CodeDeployFullAccess
- ECRFullAccess (Docker 확장시 추가)

---

## 🚀 배포 순서 (SCP)

- Github Actions : `gradle build` → `build/libs/s3board.jar` 생성
- SCP로 → EC2에 빌드된 파일 전송
- SSH로 EC2에 접속 → 배포

---



## ✅ 실습 완료시: EC2 자원 정리 필수!
