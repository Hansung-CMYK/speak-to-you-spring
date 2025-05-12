CREATE TABLE IF NOT EXISTS ego_like (
    id SERIAL PRIMARY KEY,                                   -- EGO_LIKE 고유 ID, PostgreSQL에서는 SERIAL 사용
    uid VARCHAR(255) NOT NULL,                               -- 사용자 고유 ID
    ego_id INTEGER,                                          -- ego ID
    is_like BOOLEAN DEFAULT FALSE                            -- 좋아요 여부 (TRUE : 좋아요, FALSE : 안누름)
);