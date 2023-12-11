package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;
import java.util.stream.StreamSupport;

public class GitRepoRepositoryImpl extends RepositoryImpl implements GitRepoRepository {

    private final Vertx vertx;

    public GitRepoRepositoryImpl(Vertx vertx) {
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
     * Get all entities.
     * @return entities
     */
    @Override
    public Future<RowSet<Row>> findAll() {
        return sqlClient(vertx)
                .query("""
                        SELECT git_repositories.id,
                               git_repositories.group_,
                               git_repositories.project,
                               git_repositories.host,
                               git_repositories.created_at,
                               git_repositories.is_load
                        FROM git_repositories;
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

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<GitRepoModel> mapTo(final RowSet<Row> rows) {
        return StreamSupport.stream(rows.spliterator(), false)
                .map(row -> row.toJson().mapTo(GitRepoModel.class))
                .toList();
    }

    /**
     * Deletes an object with the specified group and project name.
     *
     * @param group   Must not be null.
     * @param project Must not be null.
     */
    @Override
    public void deleteByGroupAndProject(String group, String project) {
        sqlClient(vertx)
                .preparedQuery("""
                        DELETE
                        FROM git_repositories
                        WHERE git_repositories.group_ = $1
                          AND git_repositories.project = $2
                        """)
                .execute(Tuple.of(group, project));
    }

}
