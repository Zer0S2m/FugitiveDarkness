package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.DocxFileModel;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface DocxFileRepository extends Repository<RowSet<Row>, DocxFileModel> {

    /**
     * Check entry for existence by ID.
     *
     * @param id ID repository.
     * @return Result.
     */
    Future<RowSet<Row>> existsById(long id);

    /**
     * Delete an entity from the database by id.
     *
     * @param id ID git filter
     * @return Result.
     */
    Future<RowSet<Row>> deleteById(long id);

}
