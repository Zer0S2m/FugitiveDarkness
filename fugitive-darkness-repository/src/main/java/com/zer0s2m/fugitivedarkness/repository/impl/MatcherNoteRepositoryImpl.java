package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.MatcherNoteModel;
import com.zer0s2m.fugitivedarkness.repository.MatcherNoteRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.List;

public class MatcherNoteRepositoryImpl extends RepositoryImpl implements MatcherNoteRepository {

    private final Vertx vertx;

    public MatcherNoteRepositoryImpl(Vertx vertx) {
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
                        SELECT "matcher_notes".id,
                               "matcher_notes".value,
                               "matcher_notes".file,
                               "matcher_notes".line,
                               "matcher_notes".line_number,
                               "matcher_notes".git_repository_id
                        FROM "matcher_notes";
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
    public Future<RowSet<Row>> save(MatcherNoteModel entity) {
        return null;
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<MatcherNoteModel> mapTo(RowSet<Row> rows) {
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
