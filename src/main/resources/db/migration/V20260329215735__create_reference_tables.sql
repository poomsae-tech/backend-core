-- Создание таблицы age_groups
CREATE TABLE IF NOT EXISTS age_groups (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    min_age INT NOT NULL,
    max_age INT NOT NULL,

    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by BIGINT NOT NULL DEFAULT 1,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by BIGINT
);

CREATE INDEX idx_age_groups_deleted ON age_groups(deleted);


-- Создание таблицы belt_levels
CREATE TABLE IF NOT EXISTS belt_levels (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,

    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by BIGINT NOT NULL DEFAULT 1,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by BIGINT
);

CREATE INDEX idx_belt_levels_deleted ON belt_levels(deleted);


-- Создание таблицы disciplines
CREATE TABLE IF NOT EXISTS disciplines (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,

    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by BIGINT NOT NULL DEFAULT 1,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by BIGINT
);

CREATE INDEX idx_disciplines_deleted ON disciplines(deleted);


-- Создание таблицы weight_categories
CREATE TABLE IF NOT EXISTS weight_categories (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    min_weight REAL NOT NULL,
    max_weight REAL NOT NULL,
    gender TEXT NOT NULL,

    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by BIGINT NOT NULL DEFAULT 1,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by BIGINT
);

CREATE INDEX idx_weight_categories_deleted ON weight_categories(deleted);