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
    personality VARCHAR(255),                                     -- EGO 성격 설명
    created_at DATE NOT NULL DEFAULT CURRENT_DATE                 -- 생성 날짜
);