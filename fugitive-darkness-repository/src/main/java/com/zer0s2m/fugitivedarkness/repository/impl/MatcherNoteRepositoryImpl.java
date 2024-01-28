package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.MatcherNoteModel;
import com.zer0s2m.fugitivedarkness.repository.MatcherNoteRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;
import java.util.stream.StreamSupport;

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
                        SELECT "matcher_notes"."id",
                               "matcher_notes"."value",
                               "matcher_notes"."created_at",
                               "matcher_notes"."file",
                               "matcher_notes"."line",
                               "matcher_notes"."line_number",
                               "matcher_notes"."git_repository_id"
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
        return sqlClient(vertx)
                .preparedQuery("""
                            INSERT INTO "matcher_notes" (value, file, line, line_number, git_repository_id)
                            VALUES ($1, $2, $3, $4, $5)
                            RETURNING *
                        """)
                .execute(Tuple.of(entity.getValue(), entity.getFile(),
                        entity.getLine(), entity.getLineNumber(), entity.getGitRepositoryId()));
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<MatcherNoteModel> mapTo(RowSet<Row> rows) {
        return StreamSupport.stream(rows.spliterator(), false)
                .map(row -> row.toJson().mapTo(MatcherNoteModel.class))
                .toList();
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

    /**
     * Delete note to find a match by ID.
     *
     * @param id ID matcher note.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> deleteById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        DELETE
                        FROM "matcher_notes"
                        WHERE "matcher_notes".id = $1
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Check entry for existence by ID.
     *
     * @param id ID matcher note.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> existsById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT EXISTS(select id
                                      from "matcher_notes"
                                      WHERE "matcher_notes"."id" = $1)
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Update note text by ID.
     *
     * @param id    ID matcher note.
     * @param value New meaning.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> updateValueById(long id, String value) {
        return sqlClient(vertx)
                .preparedQuery("""
                        UPDATE "matcher_notes"
                        SET value = $1
                        WHERE "matcher_notes".id = $2
                        RETURNING *
                        """)
                .execute(Tuple.of(value, id));
    }

}
