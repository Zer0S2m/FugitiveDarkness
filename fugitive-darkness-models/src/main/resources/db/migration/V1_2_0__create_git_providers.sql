CREATE TABLE IF NOT EXISTS "git_providers"
(
    id         SERIAL PRIMARY KEY NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    type       VARCHAR(255)       NOT NULL,
    is_org     BOOLEAN            NOT NULL,
    is_user    BOOLEAN            NOT NULL,
    target     VARCHAR(255)       NOT NULL
)
