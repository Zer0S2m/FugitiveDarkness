CREATE TABLE IF NOT EXISTS "git_repositories"
(
    id         SERIAL PRIMARY KEY      NOT NULL,
    group_     varchar(255)            NOT NULL,
    project    varchar(255)            NOT NULL,
    host       varchar(255)            NOT NULL,
    created_at timestamp DEFAULT now(),
    is_load    boolean   DEFAULT false NOT NULL
)
