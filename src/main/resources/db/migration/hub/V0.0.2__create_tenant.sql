-- 테넌트 정보를 저장하는 테이블
CREATE TABLE IF NOT EXISTS tenant (
    tenant_id BIGINT NOT NULL CONSTRAINT tenant_pk PRIMARY KEY,
    tenant_name VARCHAR(255) NOT NULL CONSTRAINT tenant_tenant_id_key UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE
);

-- 테넌트 테이블의 schema 컬럼에 대한 고유 인덱스 생성
-- 고유 인덱스를 추가하여 조회 속도를 향상 시키기 위함이다.
CREATE UNIQUE INDEX IF NOT EXISTS tenant_id_uniq_1399617387223
    ON tenant (tenant_name);

CREATE SEQUENCE tenant_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE tenant ALTER COLUMN tenant_id SET DEFAULT nextval('tenant_seq');