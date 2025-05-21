CREATE TABLE IF NOT EXISTS notification (
    notification_id SERIAL PRIMARY KEY,                                        -- 관계 ID, PostgreSQL에서는 SERIAL 사용
    uid VARCHAR(255) NOT NULL,                               -- 사용자 고유 ID
    ego_id INTEGER,                     -- ego ID
    title VARCHAR(255), -- 알림 제목
    created_at DATE, -- 생성 날짜
    content_html TEXT, -- 내용
    is_read BOOLEAN DEFAULT FALSE, -- 읽은 여부
    is_deleted BOOLEAN DEFAULT FALSE    -- 토픽 삭제 여부
);