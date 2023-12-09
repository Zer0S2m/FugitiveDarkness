package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.repository.Repository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class GitRepoRepository extends RepositoryImpl implements Repository<RowSet<Row>, GitRepoModel> {

    private final Vertx vertx;

    public GitRepoRepository(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Future<RowSet<Row>> findById(int id) {
        return sqlClient(vertx)
                .preparedQuery("SELECT * FROM git_repositories WHERE id=$1")
                .execute(Tuple.of(id));
    }

    /**
     * Saves a given entity.
     * @param entity Must not be null.
     * @return The saved entity
     */
    @Override
    public Future<RowSet<Row>> save(GitRepoModel entity) {
        return sqlClient(vertx)
                .preparedQuery("""
                            INSERT INTO git_repositories (group_, project, host)
                            VALUES ($1, $2, $3)
                            RETURNING git_repositories.id,
                                git_repositories.group_,
                                git_repositories.project,
                                git_repositories.created_at,
                                git_repositories.is_load
                        """)
                .execute(Tuple.of(entity.getGroup(), entity.getProject(), entity.getHost()));
    }

}
