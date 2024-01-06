package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.GitProviderModel;
import com.zer0s2m.fugitivedarkness.repository.Repository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;

public class GitProviderRepositoryImpl extends RepositoryImpl implements Repository<RowSet<Row>, GitProviderModel> {

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
     * Get all entities.
     *
     * @return entities
     */
    @Override
    public Future<RowSet<Row>> findAll() {
        return null;
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
        return null;
    }

    /**
     * Convert EXISTS request to object.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public boolean mapToExistsColumn(RowSet<Row> rows) {
        return false;
    }

}
