-- Таблица заявок на турнир
CREATE TABLE IF NOT EXISTS entry (
    id BIGSERIAL PRIMARY KEY,
    tournament_id BIGINT NOT NULL,
    coach_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL REFERENCES organizations(id) ON DELETE CASCADE,
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    rejection_reason TEXT,
    
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by BIGINT NOT NULL DEFAULT 1,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by BIGINT
);

-- Таблица спортсменов в заявке
CREATE TABLE IF NOT EXISTS entry_athlete (
    id BIGSERIAL PRIMARY KEY,
    entry_id BIGINT NOT NULL REFERENCES entry(id) ON DELETE CASCADE,
    athlete_id BIGINT NOT NULL,
    weight_category TEXT NOT NULL,
    age_group TEXT NOT NULL,
    
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by BIGINT NOT NULL DEFAULT 1,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by BIGINT,
    
    UNIQUE(entry_id, athlete_id)
);

-- Индексы
CREATE INDEX idx_entry_tournament_id ON entry(tournament_id);
CREATE INDEX idx_entry_coach_id ON entry(coach_id);
CREATE INDEX idx_entry_status ON entry(status);
CREATE INDEX idx_entry_deleted ON entry(deleted);
CREATE INDEX idx_entry_athlete_entry_id ON entry_athlete(entry_id);