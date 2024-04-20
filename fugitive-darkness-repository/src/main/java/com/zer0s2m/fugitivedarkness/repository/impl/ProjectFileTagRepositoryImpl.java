package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.ProjectFileTag;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileTagRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;
import java.util.stream.StreamSupport;

public class ProjectFileTagRepositoryImpl extends RepositoryImpl implements ProjectFileTagRepository {

    private final Vertx vertx;

    public ProjectFileTagRepositoryImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * Delete project tag to find a match by ID.
     *
     * @param id ID project tag.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> deleteById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        DELETE
                        FROM "project_file_tags"
                        WHERE "project_file_tags".id = $1
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Check entry for existence by ID.
     *
     * @param id ID project tag.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> existsById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT EXISTS(SELECT id
                                      FROM "project_file_tags"
                                      WHERE "project_file_tags"."id" = $1)
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Update tag project title by ID.
     *
     * @param id ID project tag.
     * @param title New meaning.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> updateTitleById(long id, String title) {
        return sqlClient(vertx)
                .preparedQuery("""
                        UPDATE "project_file_tags"
                        SET title = $1
                        WHERE "project_file_tags".id = $2
                        RETURNING *
                        """)
                .execute(Tuple.of(title, id));
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
                        SELECT "project_file_tags"."id",
                               "project_file_tags"."created_at",
                               "project_file_tags"."title",
                               "project_file_tags"."git_repository_id"
                        FROM "project_file_tags";
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
    public Future<RowSet<Row>> save(ProjectFileTag entity) {
        return sqlClient(vertx)
                .preparedQuery("""
                            INSERT INTO "project_file_tags" (title, git_repository_id)
                            VALUES ($1, $2)
                            RETURNING *
                        """)
                .execute(Tuple.of(entity.getTitle(), entity.getGitRepositoryId()));
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<ProjectFileTag> mapTo(RowSet<Row> rows) {
        return StreamSupport.stream(rows.spliterator(), false)
                .map(row -> row.toJson().mapTo(ProjectFileTag.class))
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

}
