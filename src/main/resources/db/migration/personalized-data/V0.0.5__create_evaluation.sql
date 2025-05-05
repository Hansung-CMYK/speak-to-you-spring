-- ego 평가
CREATE TABLE IF NOT EXISTS evaluation (
    id SERIAL PRIMARY KEY,
    uid VARCHAR(255) NOT NULL,                                 -- 사용자 고유 ID
    ego_id INT NOT NULL,                                       -- EGO 고유 ID
    solving_score INT CHECK (solving_score BETWEEN 1 AND 3),   -- 문제 해결 능력 (1~3 점)
    talking_score INT CHECK (talking_score BETWEEN 1 AND 3),   -- 공감 능력 (1~3 점)
    overall_score INT CHECK (overall_score BETWEEN 1 AND 5)   -- 총평 (1~5 점)
);