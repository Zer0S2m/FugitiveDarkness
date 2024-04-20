package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.ProjectFileColor;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileColorRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.List;

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
        return null;
    }

    /**
     * Check entry for existence by ID.
     *
     * @param id ID project color.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> existsById(long id) {
        return null;
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
        return null;
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
        return null;
    }

    /**
     * Saves a given entity.
     *
     * @param entity Must not be null.
     * @return The saved entity
     */
    @Override
    public Future<RowSet<Row>> save(ProjectFileColor entity) {
        return null;
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<ProjectFileColor> mapTo(RowSet<Row> rows) {
        return null;
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
