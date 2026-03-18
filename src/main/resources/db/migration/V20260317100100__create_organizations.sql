-- Создание таблицы организаций
CREATE TABLE IF NOT EXISTS organizations (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    inn TEXT NOT NULL,
    address TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING',
    federation_id BIGINT NOT NULL,
    region_id BIGINT NOT NULL,
    
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by BIGINT NOT NULL DEFAULT 1,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by BIGINT
);

CREATE INDEX idx_organizations_deleted ON organizations(deleted);
CREATE INDEX idx_organizations_federation_id ON organizations(federation_id);
CREATE INDEX idx_organizations_region_id ON organizations(region_id);
CREATE INDEX idx_organizations_status ON organizations(status);
