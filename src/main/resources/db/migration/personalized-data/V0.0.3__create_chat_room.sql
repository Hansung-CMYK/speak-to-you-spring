-- 채팅방 테이블 (chat_room)
CREATE TABLE IF NOT EXISTS chat_room (
    id SERIAL PRIMARY KEY,                                                  -- 채팅방 ID (자동 증가)
    uid VARCHAR(255) NOT NULL,                                              -- 사용자 고유 ID (User 테이블의 UID)
    ego_id INT NOT NULL,                                                    -- 연결된 EGO ID
    last_chat_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                       -- 최근 대화한 시간 (YYYY-MM-DD HH24:MI:SS)
    is_deleted BOOLEAN DEFAULT FALSE                                        -- 삭제 여부 (TRUE: 삭제, FALSE: 정상)
);