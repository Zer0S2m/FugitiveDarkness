package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.ProjectComment;
import com.zer0s2m.fugitivedarkness.repository.ProjectCommentRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;
import java.util.stream.StreamSupport;

public class ProjectCommentRepositoryImpl extends RepositoryImpl implements ProjectCommentRepository {

    private final Vertx vertx;

    public ProjectCommentRepositoryImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * Get an entity by unique ID key.
     *
     * @param id Must not be null.
     * @return Entity.
     */
    @Override
    public Future<RowSet<Row>> findById(int id) {
        return null;
    }

    /**
     * Get all entities.
     *
     * @return entities
     */
    @Override
    public Future<RowSet<Row>> findAll() {
        return sqlClient(vertx)
                .query("""
                        SELECT "project_comments"."id",
                               "project_comments"."text",
                               "project_comments"."created_at",
                               "project_comments"."file",
                               "project_comments"."git_repository_id"
                        FROM "project_comments";
                        """)
                .execute();
    }

    /**
     * Saves a given entity.
     *
     * @param entity Must not be null.
     * @return The saved entity
     */
    @Override
    public Future<RowSet<Row>> save(ProjectComment entity) {
        return sqlClient(vertx)
                .preparedQuery("""
                            INSERT INTO "project_comments" (text, file, git_repository_id)
                            VALUES ($1, $2, $3)
                            RETURNING *
                        """)
                .execute(Tuple.of(entity.getText(), entity.getFile(), entity.getGitRepositoryId()));
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<ProjectComment> mapTo(RowSet<Row> rows) {
        return StreamSupport.stream(rows.spliterator(), false)
                .map(row -> row.toJson().mapTo(ProjectComment.class))
                .toList();
    }

    /**
     * Convert EXISTS request to object.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public boolean mapToExistsColumn(RowSet<Row> rows) {
        return super.mapToExistsColumn(rows);
    }

    /**
     * Delete project comment to find a match by ID.
     * @param id ID project comment.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> deleteById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        DELETE
                        FROM "project_comments"
                        WHERE "project_comments".id = $1
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Check entry for existence by ID.
     *
     * @param id ID project comment.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> existsById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT EXISTS(SELECT id
                                      FROM "project_comments"
                                      WHERE "project_comments"."id" = $1)
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Update comment project text by ID.
     *
     * @param id ID matcher note.
     * @param text New meaning.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> updateTextById(long id, String text) {
        return sqlClient(vertx)
                .preparedQuery("""
                        UPDATE "project_comments"
                        SET text = $1
                        WHERE "project_comments".id = $2
                        RETURNING *
                        """)
                .execute(Tuple.of(text, id));
    }

}
