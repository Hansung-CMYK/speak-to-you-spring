-- 에고와 에고 성격(여러 개)를 매핑 시켜놓은 테이블
CREATE TABLE IF NOT EXISTS ego_personality (
    ego_personality_id SERIAL PRIMARY KEY,                            -- 고유 ID (자동 증가)
    ego_id INTEGER,                                                   -- ego의 ID
    personality_id INTEGER                                           -- 성격 정보가 들어있는 테이블의 ID
);