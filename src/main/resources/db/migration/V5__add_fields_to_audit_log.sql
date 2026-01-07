ALTER TABLE audit_logs
ADD COLUMN target_user_id UUID,
ADD COLUMN target_user_email VARCHAR(255),
ADD COLUMN message TEXT;