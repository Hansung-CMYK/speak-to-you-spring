-- 에고의 성격을 나열해놓은 테이블
CREATE TABLE IF NOT EXISTS personality (
    personality_id SERIAL PRIMARY KEY,                            -- 고유 ID (자동 증가)
    conent VARCHAR(20)                                                   -- 성격 내용
);