package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.MatcherNoteModel;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface MatcherNoteRepository extends Repository<RowSet<Row>, MatcherNoteModel> {

    /**
     * Delete note to find a match by ID.
     * @param id ID matcher note.
     * @return Result.
     */
    Future<RowSet<Row>> deleteById(long id);

    /**
     * Check entry for existence by ID.
     *
     * @param id ID matcher note.
     * @return Result.
     */
    Future<RowSet<Row>> existsById(long id);

    /**
     * Update note text by ID.
     *
     * @param id ID matcher note.
     * @param value New meaning.
     * @return Result.
     */
    Future<RowSet<Row>> updateValueById(long id, String value);

}
