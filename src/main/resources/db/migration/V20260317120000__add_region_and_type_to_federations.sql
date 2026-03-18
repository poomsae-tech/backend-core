-- Добавление колонок region_id и federation_type в таблицу federations
ALTER TABLE federations
  ADD COLUMN IF NOT EXISTS region_id BIGINT NOT NULL DEFAULT 1,
  ADD COLUMN IF NOT EXISTS federation_type TEXT NOT NULL DEFAULT 'ALL_RUSSIAN';

CREATE INDEX IF NOT EXISTS idx_federations_region_id ON federations(region_id);
CREATE INDEX IF NOT EXISTS idx_federations_federation_type ON federations(federation_type);
