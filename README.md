# Running locally
1. DB 생성
   - hub, tenant라는 이름의 database를 생성한다.
   - hub: 통합 정보를 관리하는 데이터베이스 (ex: 유저, 일기, 통계정보 등)
   - tenant: 개별 정보를 관리하는 데이터베이스 (ex: 대화 내역, 일기 등)
2. root로 접근이 안되는 경우 (수정 예정)
   - user1, user1234을 생성해서 권한을 부여
   - 또는, application.yml의 유저 정보를 root로 변경
3. SpringBoot 실행 
