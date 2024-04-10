package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.GitJobModel;
import com.zer0s2m.fugitivedarkness.repository.GitJobRepository;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.List;

public class GitJobRepositoryImpl extends RepositoryImpl implements GitJobRepository {

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
    public Future<RowSet<Row>> save(GitJobModel entity) {
        return null;
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<GitJobModel> mapTo(RowSet<Row> rows) {
        return null;
    }

    /**
     * Convert EXISTS request to object.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public boolean mapToExistsColumn(final RowSet<Row> rows) {
        return super.mapToExistsColumn(rows);
    }

}
