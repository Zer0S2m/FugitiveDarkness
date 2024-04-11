package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.GitJobModel;
import com.zer0s2m.fugitivedarkness.repository.GitJobRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;
import java.util.stream.StreamSupport;

public class GitJobRepositoryImpl extends RepositoryImpl implements GitJobRepository {

    private final Vertx vertx;

    public GitJobRepositoryImpl(Vertx vertx) {
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
                        SELECT "git_jobs"."id",
                               "git_jobs"."created_at",
                               "git_jobs"."type",
                               "git_jobs"."cron",
                               "git_jobs"."git_repository_id"
                        FROM "git_jobs";
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
    public Future<RowSet<Row>> save(GitJobModel entity) {
        return sqlClient(vertx)
                .preparedQuery("""
                        INSERT INTO "git_jobs" (git_repository_id,
                                                type,
                                                cron)
                        VALUES ($1, $2, $3)
                        RETURNING *
                        """)
                .execute(Tuple.of(
                        entity.getGitRepositoryId(),
                        entity.getType(),
                        entity.getCron()));
    }

    /**
     * Delete job to find a match by ID.
     *
     * @param id ID jit job.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> deleteById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        DELETE
                        FROM "git_jobs"
                        WHERE "git_jobs".id = $1
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Check entry for existence by ID.
     *
     * @param id ID git job.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> existsById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT EXISTS(select id
                                      from "git_jobs"
                                      WHERE "git_jobs"."id" = $1)
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Update the cron field entity by ID.
     *
     * @param cron New value.
     * @param id   ID git job.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> updateCronById(String cron, long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        UPDATE "git_jobs"
                        SET cron = $1
                        WHERE id = $2
                        RETURNING *
                        """)
                .execute(Tuple.of(cron, id));
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<GitJobModel> mapTo(RowSet<Row> rows) {
        return StreamSupport.stream(rows.spliterator(), false)
                .map(row -> row.toJson().mapTo(GitJobModel.class))
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

}
