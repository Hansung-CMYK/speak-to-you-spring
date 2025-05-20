CREATE TABLE IF NOT EXISTS user_account (
    uid VARCHAR(255) NOT NULL CONSTRAINT user_account_pk PRIMARY KEY, -- Firebase UID
    ego_id INTEGER,                                                   -- ego ID
    email VARCHAR(255),                                               -- email 주소
    birth_date DATE,                                                  -- 생년월일 (YYYY-MM-DD)
    role VARCHAR(50),                                                 -- 사용자 역할 (ROLE_USER, ROLE_ADMIN 등)
    created_at DATE,                                                  -- 생성일시
    is_deleted BOOLEAN DEFAULT FALSE                                  -- 탈퇴 여부(TRUE : 탈퇴, FALSE : 정상)
    );

CREATE TABLE IF NOT EXISTS ego (
    id SERIAL PRIMARY KEY,                                        -- EGO 고유 ID, PostgreSQL에서는 SERIAL 사용
    name VARCHAR(100) NOT NULL,                                   -- EGO 이름
    introduction TEXT,                                            -- EGO 자기소개
    profile_image BYTEA,                                          -- EGO 프로필 이미지, PostgreSQL에서는 BYTEA 사용
    mbti VARCHAR(4),                                              -- MBTI 성격 유형
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,                -- 생성 날짜
    likes INTEGER                                                 -- 에고별 좋아요 개수
);

-- 에고와 에고 성격(여러 개)를 매핑 시켜놓은 테이블
CREATE TABLE IF NOT EXISTS ego_personality (
    ego_personality_id SERIAL PRIMARY KEY,                            -- 고유 ID (자동 증가)
    ego_id INTEGER,                                                   -- ego의 ID
    personality_id INTEGER                                           -- 성격 정보가 들어있는 테이블의 ID
);

-- 에고의 성격을 나열해놓은 테이블
CREATE TABLE IF NOT EXISTS personality (
    personality_id SERIAL PRIMARY KEY,                            -- 고유 ID (자동 증가)
    conent VARCHAR(20),                                                   -- 성격 내용
    image_url VARCHAR(255) -- 프론트 이미지 경로
);