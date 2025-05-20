CREATE TABLE IF NOT EXISTS relationship (
    relationship_id SERIAL PRIMARY KEY,                                        -- 관계 ID, PostgreSQL에서는 SERIAL 사용
    relationship_content VARCHAR(20) -- 관계 내용
);