package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.GitFilterModel;
import com.zer0s2m.fugitivedarkness.repository.GitFilterRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;

public class GitFilterRepositoryImpl extends RepositoryImpl implements GitFilterRepository {

    private final Vertx vertx;

    public GitFilterRepositoryImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * Check entry for existence by ID.
     *
     * @param id ID git filter.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> existsById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT EXISTS(select id
                                      from "git_filters"
                                      WHERE "git_filters"."id" = $1)
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Delete an entity from the database by id.
     *
     * @param id ID git filter
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> deleteById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        DELETE
                        FROM "git_filters"
                        WHERE "git_filters".id = $1
                        """)
                .execute(Tuple.of(id));
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
    public Future<RowSet<Row>> save(GitFilterModel entity) {
        return null;
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<GitFilterModel> mapTo(RowSet<Row> rows) {
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
