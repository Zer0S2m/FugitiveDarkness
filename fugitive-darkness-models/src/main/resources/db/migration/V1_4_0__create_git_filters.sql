CREATE TABLE IF NOT EXISTS "git_filters"
(
    id         SERIAL PRIMARY KEY NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    title      VARCHAR(255)       NOT NULL,
    filter     JSONB              NOT NULL
)
