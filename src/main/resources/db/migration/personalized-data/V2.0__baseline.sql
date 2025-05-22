-- 채팅방 테이블 (chat_room)
CREATE TABLE IF NOT EXISTS chat_room (
    id SERIAL PRIMARY KEY,                                                  -- 채팅방 ID (자동 증가)
    uid VARCHAR(255) NOT NULL,                                              -- 사용자 고유 ID (User 테이블의 UID)
    ego_id INT NOT NULL,                                                    -- 연결된 EGO ID
    last_chat_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                       -- 최근 대화한 시간 (YYYY-MM-DD HH24:MI:SS)
    is_deleted BOOLEAN DEFAULT FALSE                                        -- 삭제 여부 (TRUE: 삭제, FALSE: 정상)
);

-- 채팅 내역 테이블 (chat_history)
CREATE TABLE IF NOT EXISTS chat_history (
    id SERIAL PRIMARY KEY,                                   -- 채팅 내역 ID (자동 증가)
    uid VARCHAR(255) NOT NULL,                               -- 사용자 고유 ID
    chat_room_id INT NOT NULL,                               -- 채팅방 ID
    content TEXT,                                            -- 사용자가 보낸 메시지
    type VARCHAR(1) NOT NULL,                                -- 대화 유형 (U - User, E - Ego)
    chat_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,             -- 대화가 발생한 시간 (현재 시간)
    is_deleted BOOLEAN DEFAULT FALSE,                         -- 삭제 여부 (TRUE : 삭제, FALSE : 정상)
    message_hash VARCHAR(64),
    content_type VARCHAR(255) DEFAULT 'text'
);


-- ego 평가
CREATE TABLE IF NOT EXISTS evaluation (
    id SERIAL PRIMARY KEY,
    uid VARCHAR(255) NOT NULL,                                 -- 사용자 고유 ID
    ego_id INT NOT NULL,                                       -- EGO 고유 ID
    solving_score INT CHECK (solving_score BETWEEN 1 AND 3),   -- 문제 해결 능력 (1~3 점)
    talking_score INT CHECK (talking_score BETWEEN 1 AND 3),   -- 공감 능력 (1~3 점)
    overall_score INT CHECK (overall_score BETWEEN 1 AND 5)   -- 총평 (1~5 점)
);

-- ego 별 좋아요 테이블
CREATE TABLE IF NOT EXISTS ego_like (
    id SERIAL PRIMARY KEY,              -- EGO_LIKE 고유 ID, PostgreSQL에서는 SERIAL 사용
    uid VARCHAR(255) NOT NULL,          -- 사용자 고유 ID
    ego_id INTEGER,                     -- ego ID
    is_like BOOLEAN DEFAULT FALSE       -- 좋아요 여부 (TRUE : 좋아요, FALSE : 안누름)
);

-- 일기 테이블
CREATE TABLE IF NOT EXISTS diary (
    diary_id SERIAL PRIMARY KEY,        -- DIARY 고유 ID, PostgreSQL에서는 SERIAL 사용
    uid VARCHAR(255) NOT NULL,          -- 사용자 고유 ID
    ego_id INTEGER,                     -- ego ID
    feeling VARCHAR(50),                -- 오늘의 감정
    created_at DATE,                    -- 일기 생성 날짜
    daily_comment VARCHAR(255)          -- 오늘의 한 줄 요약
);

-- 일기의 주제별 테이블
CREATE TABLE IF NOT EXISTS topic (
    topic_id SERIAL PRIMARY KEY,        -- topic 고유 ID, PostgreSQL에서는 SERIAL 사용
    diary_id INTEGER,                   -- DIARY 고유 ID
    title VARCHAR(255),                 -- 일기 내 토픽 주제
    content TEXT,                       -- 일기 내 토픽의 내용
    url VARCHAR(2048),             -- 토픽 별 사진
    is_deleted BOOLEAN DEFAULT FALSE    -- 토픽 삭제 여부
);

-- 일기의 들어가는 키워드 테이블
CREATE TABLE IF NOT EXISTS diary_keyword (
    keyword_id SERIAL PRIMARY KEY, -- keyword_id 고유 ID, PostgreSQL에서는 SERIAL 사용
    diary_id INTEGER, -- DIARY 고유 ID
    content VARCHAR(255) -- 키워드 내용
);