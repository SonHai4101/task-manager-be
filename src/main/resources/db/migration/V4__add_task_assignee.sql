ALTER TABLE tasks
ADD COLUMN assigned_to_id UUID;

ALTER TABLE tasks
ADD CONSTRAINT fk_task_assignee
FOREIGN KEY (assigned_to_id)
REFERENCES users(id)
ON DELETE SET NULL;