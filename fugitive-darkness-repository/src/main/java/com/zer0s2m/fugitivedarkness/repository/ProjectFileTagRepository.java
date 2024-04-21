package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.ProjectFileTag;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface ProjectFileTagRepository extends Repository<RowSet<Row>, ProjectFileTag> {

    /**
     * Delete project tag to find a match by ID.
     *
     * @param id ID project tag.
     * @return Result.
     */
    Future<RowSet<Row>> deleteById(long id);

    /**
     * Check entry for existence by ID.
     *
     * @param id ID project tag.
     * @return Result.
     */
    Future<RowSet<Row>> existsById(long id);

    /**
     * Update tag project title by ID.
     *
     * @param id ID project tag.
     * @param title New meaning.
     * @return Result.
     */
    Future<RowSet<Row>> updateTitleById(long id, String title);

}
