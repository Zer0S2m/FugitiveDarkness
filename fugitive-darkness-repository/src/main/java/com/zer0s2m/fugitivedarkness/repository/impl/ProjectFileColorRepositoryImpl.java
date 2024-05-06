package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.ProjectFileColor;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileColorRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;
import java.util.stream.StreamSupport;

public class ProjectFileColorRepositoryImpl extends RepositoryImpl implements ProjectFileColorRepository {

    private final Vertx vertx;

    public ProjectFileColorRepositoryImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * Delete project color to find a match by ID.
     *
     * @param id ID project color.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> deleteById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        DELETE
                        FROM "project_file_colors"
                        WHERE "project_file_colors".id = $1
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Check entry for existence by ID.
     *
     * @param id ID project color.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> existsById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT EXISTS(SELECT id
                                      FROM "project_file_colors"
                                      WHERE "project_file_colors"."id" = $1)
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Update color project text by ID.
     *
     * @param id    ID color project.
     * @param color New meaning.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> updateColorById(long id, String color) {
        return sqlClient(vertx)
                .preparedQuery("""
                        UPDATE "project_file_colors"
                        SET color = $1
                        WHERE "project_file_colors".id = $2
                        RETURNING *
                        """)
                .execute(Tuple.of(color, id));
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
                        SELECT "project_file_colors"."id",
                               "project_file_colors"."created_at",
                               "project_file_colors"."color",
                               "project_file_colors"."git_repository_id"
                        FROM "project_file_colors";
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
    public Future<RowSet<Row>> save(ProjectFileColor entity) {
        return sqlClient(vertx)
                .preparedQuery("""
                            INSERT INTO "project_file_colors" (color, git_repository_id)
                            VALUES ($1, $2)
                            RETURNING *
                        """)
                .execute(Tuple.of(entity.getColor(), entity.getGitRepositoryId()));
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<ProjectFileColor> mapTo(RowSet<Row> rows) {
        return StreamSupport.stream(rows.spliterator(), false)
                .map(row -> row.toJson().mapTo(ProjectFileColor.class))
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
