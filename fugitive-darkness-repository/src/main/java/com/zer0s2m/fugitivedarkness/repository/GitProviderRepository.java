package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.GitProviderModel;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface GitProviderRepository extends Repository<RowSet<Row>, GitProviderModel> {

    /**
     * Find a record by provider type and target.
     *
     * @param type   Provider type.
     * @param target Provider target.
     * @return Result.
     */
    Future<RowSet<Row>> findByTypeAndTarget(String type, String target);

    /**
     * Check for the existence of an entry by provider type and provider target.
     *
     * @param type   Provider type.
     * @param target Provider target
     * @return Is exists.
     */
    Future<RowSet<Row>> existsByTypeAndTarget(String type, String target);

    /**
     * Delete a record from the database by provider type and target.
     *
     * @param type Provider type.
     * @param target Provider target.
     */
    Future<RowSet<Row>> deleteByTypeAndTarget(String type, String target);

}
