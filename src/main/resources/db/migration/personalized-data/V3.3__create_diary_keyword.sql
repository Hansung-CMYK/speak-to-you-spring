CREATE TABLE IF NOT EXISTS diary_keyword (
    keyword_id SERIAL PRIMARY KEY, -- keyword_id 고유 ID, PostgreSQL에서는 SERIAL 사용
    diary_id INTEGER, -- DIARY 고유 ID
    content VARCHAR(255) -- 키워드 내용
);