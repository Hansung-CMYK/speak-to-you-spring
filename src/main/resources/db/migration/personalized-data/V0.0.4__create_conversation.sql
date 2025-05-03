-- 채팅 내역 테이블 (chat_history)
CREATE TABLE IF NOT EXISTS chat_history (
    id SERIAL PRIMARY KEY,                                   -- 채팅 내역 ID (자동 증가)
    uid VARCHAR(255) NOT NULL,                               -- 사용자 고유 ID
    chat_room_id INT NOT NULL,                               -- 채팅방 ID
    content TEXT,                                            -- 사용자가 보낸 메시지
    type VARCHAR(1) NOT NULL,                                -- 대화 유형 (U - User, E - Ego)
    chat_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,             -- 대화가 발생한 시간 (현재 시간)
    is_deleted BOOLEAN DEFAULT FALSE                         -- 삭제 여부 (TRUE : 삭제, FALSE : 정상)
);
