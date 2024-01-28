package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.GitFilterModel;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface GitFilterRepository extends Repository<RowSet<Row>, GitFilterModel> {

    /**
     * Check entry for existence by ID.
     *
     * @param id ID git filter.
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
