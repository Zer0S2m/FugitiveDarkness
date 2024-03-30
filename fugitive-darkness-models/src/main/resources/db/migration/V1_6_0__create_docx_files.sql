CREATE TABLE IF NOT EXISTS "docx_files"
(
    id           SERIAL PRIMARY KEY NOT NULL,
    created_at   TIMESTAMP DEFAULT now(),
    path         TEXT               NOT NULL,
    title        VARCHAR(2048)      NOT NULL,
    origin_title VARCHAR(2048)      NOT NULL
)
