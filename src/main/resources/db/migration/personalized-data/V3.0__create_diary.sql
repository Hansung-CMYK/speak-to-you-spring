CREATE TABLE IF NOT EXISTS diary (
    diary_id SERIAL PRIMARY KEY, -- DIARY 고유 ID, PostgreSQL에서는 SERIAL 사용
    uid VARCHAR(255) NOT NULL,                               -- 사용자 고유 ID
    ego_id INTEGER,                                          -- ego ID
    feeling VARCHAR(50), -- 오늘의 감정
    created_at DATE -- 일기 생성 날짜
);