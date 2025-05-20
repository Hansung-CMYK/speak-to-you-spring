CREATE TABLE IF NOT EXISTS ego_relationship (
    ego_relationship_id SERIAL PRIMARY KEY,                                        -- EGO 관계 ID, PostgreSQL에서는 SERIAL 사용
    uid VARCHAR(255),                                                   -- user ID
    ego_id INTEGER,                                                   -- ego ID
    relationship_id INTEGER -- 관계 테이블 ID
);