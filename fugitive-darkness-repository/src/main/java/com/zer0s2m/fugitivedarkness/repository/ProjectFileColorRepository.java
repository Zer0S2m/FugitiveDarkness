package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.ProjectFileColor;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface ProjectFileColorRepository extends Repository<RowSet<Row>, ProjectFileColor> {

    /**
     * Delete project color to find a match by ID.
     * @param id ID project color.
     * @return Result.
     */
    Future<RowSet<Row>> deleteById(long id);

    /**
     * Check entry for existence by ID.
     *
     * @param id ID project color.
     * @return Result.
     */
    Future<RowSet<Row>> existsById(long id);

    /**
     * Update color project text by ID.
     *
     * @param id ID color project.
     * @param color New meaning.
     * @return Result.
     */
    Future<RowSet<Row>> updateColorById(long id, String color);

}
