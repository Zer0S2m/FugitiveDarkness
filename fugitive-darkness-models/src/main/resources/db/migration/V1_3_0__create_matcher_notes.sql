CREATE TABLE IF NOT EXISTS "matcher_notes"
(
    id                SERIAL PRIMARY KEY NOT NULL,
    created_at        TIMESTAMP DEFAULT now(),
    value             VARCHAR(255)       NOT NULL,
    file              TEXT               NOT NULL,
    line              TEXT               NOT NULL,
    line_number        INT                NOT NULL,
    git_repository_id INT                NOT NULL
        CONSTRAINT git_repository_id_constraint REFERENCES "git_repositories" (id)
            ON DELETE CASCADE
)
