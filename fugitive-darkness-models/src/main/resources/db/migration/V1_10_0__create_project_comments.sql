CREATE TABLE IF NOT EXISTS "project_comments"
(
    id                SERIAL PRIMARY KEY      NOT NULL,
    git_repository_id INT                     NOT NULL
        CONSTRAINT git_repository_id_constraint REFERENCES "git_repositories" (id)
            ON DELETE CASCADE,
    text              TEXT                    NOT NULL,
    file              TEXT                    NOT NULL,
    created_at        timestamp DEFAULT now() NOT NULL
)
