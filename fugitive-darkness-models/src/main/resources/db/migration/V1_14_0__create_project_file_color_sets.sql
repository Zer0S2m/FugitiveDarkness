CREATE TABLE IF NOT EXISTS "project_file_color_sets"
(
    id                    SERIAL PRIMARY KEY      NOT NULL,
    project_file_color_id INT                     NOT NULL
        CONSTRAINT project_file_color_id_constraint REFERENCES "project_file_colors" (id)
            ON DELETE CASCADE,
    file                  TEXT                    NOT NULL,
    created_at            timestamp DEFAULT now() NOT NULL
)
