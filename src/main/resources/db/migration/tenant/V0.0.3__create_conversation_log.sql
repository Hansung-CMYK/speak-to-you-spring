CREATE TABLE IF NOT EXISTS conversation_log (
                                                log_id BIGINT PRIMARY KEY,
                                                topic VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- 생성 시간 (현재 시간)
    );

CREATE SEQUENCE conversation_log_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE conversation_log ALTER COLUMN log_id SET DEFAULT nextval('conversation_log_seq');