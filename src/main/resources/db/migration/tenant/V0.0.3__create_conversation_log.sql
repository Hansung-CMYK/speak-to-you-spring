CREATE TABLE IF NOT EXISTS conversation_log (
    log_id BIGINT PRIMARY KEY,
    topic VARCHAR(255),
    last_question VARCHAR(255),
    order_index INT NOT NULL DEFAULT 1,
    status VARCHAR(1) DEFAULT 'A' -- 'D' : 삭제됨(Delete), 'A' : 현재 사용됨 (Active)
);

CREATE SEQUENCE conversation_log_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE conversation_log ALTER COLUMN log_id SET DEFAULT nextval('conversation_log_seq');