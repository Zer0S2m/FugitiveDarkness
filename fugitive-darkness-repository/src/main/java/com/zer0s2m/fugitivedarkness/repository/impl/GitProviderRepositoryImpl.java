package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.GitProviderModel;
import com.zer0s2m.fugitivedarkness.repository.GitProviderRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;
import java.util.stream.StreamSupport;

public class GitProviderRepositoryImpl extends RepositoryImpl implements GitProviderRepository {

    private final Vertx vertx;

    public GitProviderRepositoryImpl(Vertx vertx) {
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
     * Find a record by provider type and target.
     *
     * @param type   Provider type.
     * @param target Provider target.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> findByTypeAndTarget(String type, String target) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT "git_providers".id,
                               "git_providers".created_at,
                               "git_providers".type,
                               "git_providers".is_org,
                               "git_providers".is_user,
                               "git_providers".target
                        FROM "git_providers"
                        WHERE "git_providers".type = $1
                          AND target = $2
                        """)
                .execute(Tuple.of(type, target));
    }

    /**
     * Check for the existence of an entry by provider type and provider target.
     *
     * @param type   Provider type.
     * @param target Provider target
     * @return Is exists.
     */
    @Override
    public Future<RowSet<Row>> existsByTypeAndTarget(String type, String target) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT EXISTS(select id
                                      from "git_providers"
                                      WHERE "git_providers".type = $1
                                        AND "git_providers".target = $2)
                        """)
                .execute(Tuple.of(type, target));
    }

    /**
     * Delete a record from the database by provider type and target.
     *
     * @param type Provider type.
     * @param target Provider target.
     */
    @Override
    public Future<RowSet<Row>> deleteByTypeAndTarget(String type, String target) {
        return sqlClient(vertx)
                .preparedQuery("""
                        DELETE
                        FROM "git_providers"
                        WHERE "git_providers".type = $1
                          AND "git_providers".target = $2
                        """)
                .execute(Tuple.of(type, target));
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
                        SELECT "git_providers"."id",
                               "git_providers"."created_at",
                               "git_providers"."type",
                               "git_providers"."is_org",
                               "git_providers"."is_user",
                               "git_providers"."target"
                        FROM "git_providers";
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
    public Future<RowSet<Row>> save(GitProviderModel entity) {
        return sqlClient(vertx)
                .preparedQuery("""
                            INSERT INTO "git_providers" (type, is_org, is_user, target)
                            VALUES ($1, $2, $3, $4)
                            RETURNING "git_providers".id,
                                "git_providers".type,
                                "git_providers".is_org,
                                "git_providers".is_user,
                                "git_providers".created_at,
                                "git_providers".target
                        """)
                .execute(Tuple.of(entity.getType(), entity.getIsOrg(), entity.getIsUser(), entity.getTarget()));
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<GitProviderModel> mapTo(RowSet<Row> rows) {
        return StreamSupport.stream(rows.spliterator(), false)
                .map(row -> row.toJson().mapTo(GitProviderModel.class))
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
