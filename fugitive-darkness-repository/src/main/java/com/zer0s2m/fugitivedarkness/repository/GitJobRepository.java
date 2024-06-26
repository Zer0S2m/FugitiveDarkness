package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.GitJobModel;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.time.LocalDateTime;

public interface GitJobRepository extends Repository<RowSet<Row>, GitJobModel> {

    /**
     * Find all entities by type.
     *
     * @param type The type of scheduled tasks.
     * @return Result.
     */
    Future<RowSet<Row>> findAllByType(String type);

    /**
     * Delete job to find a match by ID.
     *
     * @param id ID jit job.
     * @return Result.
     */
    Future<RowSet<Row>> deleteById(long id);

    /**
     * Check entry for existence by ID.
     *
     * @param id ID git job.
     * @return Result.
     */
    Future<RowSet<Row>> existsById(long id);

    /**
     * Update the cron field entity by ID.
     *
     * @param cron New value.
     * @param id   ID git job.
     * @return Result.
     */
    Future<RowSet<Row>> updateCronById(String cron, long id);

    /**
     * Update the next run at field entity by ID.
     *
     * @param nextRunAt New value.
     * @param id   ID git job.
     * @return Result.
     */
    Future<RowSet<Row>> updateNextRunAtById(LocalDateTime nextRunAt, long id);

}
