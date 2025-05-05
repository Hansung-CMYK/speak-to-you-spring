CREATE TABLE IF NOT EXISTS user_account (
    uid VARCHAR(255) NOT NULL CONSTRAINT user_account_pk PRIMARY KEY, -- Firebase UID
    ego_id INTEGER,                                              -- ego ID
    email VARCHAR(255),                                               -- email 주소
    birth_date DATE,                                                  -- 생년월일 (YYYY-MM-DD)
    role VARCHAR(50),                                                 -- 사용자 역할 (ROLE_USER, ROLE_ADMIN 등)
    created_at DATE,                                                  -- 생성일시
    is_deleted BOOLEAN DEFAULT FALSE                                  -- 탈퇴 여부(TRUE : 탈퇴, FALSE : 정상)
);