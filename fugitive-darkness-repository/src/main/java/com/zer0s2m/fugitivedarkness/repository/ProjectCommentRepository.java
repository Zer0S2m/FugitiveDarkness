package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.ProjectComment;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface ProjectCommentRepository extends Repository<RowSet<Row>, ProjectComment> {

    /**
     * Delete project comment to find a match by ID.
     * @param id ID project comment.
     * @return Result.
     */
    Future<RowSet<Row>> deleteById(long id);

    /**
     * Check entry for existence by ID.
     *
     * @param id ID project comment.
     * @return Result.
     */
    Future<RowSet<Row>> existsById(long id);

    /**
     * Update comment project text by ID.
     *
     * @param id ID matcher note.
     * @param text New meaning.
     * @return Result.
     */
    Future<RowSet<Row>> updateTextById(long id, String text);

}
