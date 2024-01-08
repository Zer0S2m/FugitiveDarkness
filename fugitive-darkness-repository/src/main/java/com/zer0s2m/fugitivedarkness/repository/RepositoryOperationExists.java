package com.zer0s2m.fugitivedarkness.repository;

import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.concurrent.atomic.AtomicBoolean;

public interface RepositoryOperationExists {

    /**
     * Convert EXISTS request to object.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    default boolean mapToExistsColumn(RowSet<Row> rows) {
        AtomicBoolean isExists = new AtomicBoolean();
        rows.forEach(row -> isExists.set(row.getBoolean("exists")));
        return isExists.get();
    }

}
