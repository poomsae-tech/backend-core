ALTER TABLE federations 
  ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;

CREATE INDEX idx_federations_deleted ON federations(deleted);
