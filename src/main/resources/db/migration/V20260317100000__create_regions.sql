-- Создание таблицы регионов
CREATE TABLE IF NOT EXISTS regions (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by BIGINT NOT NULL DEFAULT 1,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by BIGINT
);

CREATE INDEX idx_regions_deleted ON regions(deleted);
