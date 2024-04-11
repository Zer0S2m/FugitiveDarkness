CREATE TABLE IF NOT EXISTS "git_jobs"
(
    id                SERIAL PRIMARY KEY NOT NULL,
    git_repository_id INT                NOT NULL
        CONSTRAINT git_repository_id_constraint REFERENCES "git_repositories" (id)
            ON DELETE CASCADE,
    type              VARCHAR(255)       NOT NULL,
    cron              VARCHAR(255)       NOT NULL,
    created_at        timestamp DEFAULT now() NOT NULL
)
