ALTER TABLE "git_repositories"
    ADD COLUMN is_local BOOLEAN NOT NULL DEFAULT FALSE;