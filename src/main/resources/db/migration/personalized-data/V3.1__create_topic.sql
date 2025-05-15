CREATE TABLE IF NOT EXISTS topic (
    topic_id SERIAL PRIMARY KEY, -- topic 고유 ID, PostgreSQL에서는 SERIAL 사용
    diary_id INTEGER, -- DIARY 고유 ID
    title VARCHAR(255), -- 일기 내 토픽 주제
    content TEXT, -- 일기 내 토픽의 내용
    picture BYTEA, -- 토픽 별 사진
    is_deleted BOOLEAN DEFAULT FALSE -- 토픽 삭제 여부
);