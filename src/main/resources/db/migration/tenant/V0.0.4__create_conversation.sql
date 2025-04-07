-- 대화(Talk) 테이블 (에고와 사용자의 대화 기록)
CREATE TABLE IF NOT EXISTS conversation (
    conversation_id BIGINT PRIMARY KEY,  -- 자동 증가 (BIGSERIAL)
    log_id BIGINT NOT NULL,
    content VARCHAR(255), -- 대화 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간 (현재 시간)
    type CHAR(1) NOT NULL, -- ego질문(q), 사용자 답변(a)
    CONSTRAINT fk_conversation_log FOREIGN KEY (log_id) REFERENCES conversation_log(log_id)
);

CREATE SEQUENCE conversation_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE conversation ALTER COLUMN conversation_id SET DEFAULT nextval('conversation_seq');