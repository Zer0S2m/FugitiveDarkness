package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.ProjectFileColorSet;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileColorSetRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;
import java.util.stream.StreamSupport;

public class ProjectFileColorSetRepositoryImpl extends RepositoryImpl implements ProjectFileColorSetRepository {

    private final Vertx vertx;

    public ProjectFileColorSetRepositoryImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public Future<RowSet<Row>> setColorByIdToFile(long id, String file) {
        return sqlClient(vertx)
                .preparedQuery("""
                            INSERT INTO "project_file_color_sets" (project_file_color_id, file)
                            VALUES ($1, $2)
                            RETURNING *
                        """)
                .execute(Tuple.of(id, file));
    }

    @Override
    public Future<RowSet<Row>> unsetColorByIdToFile(long id, String file) {
        return sqlClient(vertx)
                .preparedQuery("""
                        DELETE
                        FROM "project_file_color_sets"
                        WHERE "project_file_color_sets"."project_file_color_id" = $1
                            AND "project_file_color_sets"."file" = $2
                        """)
                .execute(Tuple.of(id, file));
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
                        SELECT "project_file_color_sets"."id",
                               "project_file_color_sets"."created_at",
                               "project_file_color_sets"."file",
                               "project_file_color_sets"."project_file_color_id"
                        FROM "project_file_color_sets";
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
    public Future<RowSet<Row>> save(ProjectFileColorSet entity) {
        return null;
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<ProjectFileColorSet> mapTo(RowSet<Row> rows) {
        return StreamSupport.stream(rows.spliterator(), false)
                .map(row -> row.toJson().mapTo(ProjectFileColorSet.class))
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
