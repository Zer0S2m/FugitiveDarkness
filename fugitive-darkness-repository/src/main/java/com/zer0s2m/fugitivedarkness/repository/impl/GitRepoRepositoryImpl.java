package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;

public class GitRepoRepositoryImpl extends RepositoryImpl implements GitRepoRepository {

    private final Vertx vertx;

    public GitRepoRepositoryImpl(Vertx vertx) {
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
        return sqlClient(vertx)
                .preparedQuery("SELECT * FROM git_repositories WHERE id=$1")
                .execute(Tuple.of(id));
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
                        SELECT git_repositories.id,
                               git_repositories.group_,
                               git_repositories.project,
                               git_repositories.host,
                               git_repositories.created_at,
                               git_repositories.source,
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
                            INSERT INTO git_repositories (group_, project, host, source)
                            VALUES ($1, $2, $3, $4)
                            RETURNING git_repositories.id,
                                git_repositories.group_,
                                git_repositories.project,
                                git_repositories.created_at,
                                git_repositories.source,
                                git_repositories.is_load
                        """)
                .execute(Tuple.of(entity.getGroup(), entity.getProject(), entity.getHost(), entity.getSource()));
    }

    /**
     * Save all entities to the database.
     *
     * @param entities Entities.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> saveAll(Collection<GitRepoModel> entities) {
        List<Tuple> batch = new ArrayList<>();

        entities.forEach(entity -> batch.add(
                Tuple.of(entity.getGroup(), entity.getProject(), entity.getHost(), entity.getSource())));

        return sqlClient(vertx)
                .preparedQuery("""
                            INSERT INTO git_repositories (group_, project, host, source)
                            VALUES ($1, $2, $3, $4)
                            RETURNING git_repositories.id,
                                git_repositories.group_,
                                git_repositories.project,
                                git_repositories.created_at,
                                git_repositories.source,
                                git_repositories.is_load
                        """)
                .executeBatch(batch);
    }

    /**
     * Find all entities by project group and remote host.
     *
     * @param group Project group.
     * @param host Remote host.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> findAllByGroupAndHost(String group, String host) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT "git_repositories"."id",
                                "git_repositories"."group_",
                                "git_repositories"."project",
                                "git_repositories"."host",
                                "git_repositories"."created_at",
                                "git_repositories"."source",
                                "git_repositories"."is_load"
                        FROM "git_repositories"
                        WHERE "git_repositories"."group_" = $1 AND "git_repositories"."host" = $2
                        """)
                .execute(Tuple.of(group, host));
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
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
     * Convert EXISTS request to object.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public boolean mapToExistsColumn(final RowSet<Row> rows) {
        return super.mapToExistsColumn(rows);
    }

    /**
     * Deletes an object with the specified group and project name.
     *
     * @param group   Must not be null.
     * @param project Must not be null.
     */
    @Override
    public Future<RowSet<Row>> deleteByGroupAndProject(String group, String project) {
        return sqlClient(vertx)
                .preparedQuery("""
                        DELETE
                        FROM git_repositories
                        WHERE git_repositories.group_ = $1
                          AND git_repositories.project = $2
                        """)
                .execute(Tuple.of(group, project));
    }

    /**
     * Check entry for existence by group and project.
     *
     * @param group   Git repository group.
     * @param project Name of the project.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> existsByGroupAndProject(String group, String project) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT EXISTS(select id
                                      from "git_repositories"
                                      WHERE "git_repositories".group_ = $1
                                        AND "git_repositories".project = $2)
                        """)
                .execute(Tuple.of(group, project));
    }

    /**
     * Check entry for existence by ID.
     *
     * @param id ID repository.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> existsById(long id) {
        return sqlClient(vertx)
                .preparedQuery("""
                        SELECT EXISTS(select id
                                      from "git_repositories"
                                      WHERE "git_repositories"."id" = $1)
                        """)
                .execute(Tuple.of(id));
    }

    /**
     * Update the "loaded" attribute by group and project.
     *
     * @param group   Must not be null.
     * @param project Must not be null.
     * @param isLoad  Attribute. Must not be null.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> updateIsLoadByGroupAndProject(String group, String project, boolean isLoad) {
        return sqlClient(vertx)
                .preparedQuery("""
                        UPDATE git_repositories
                        SET is_load = $1
                        WHERE group_ = $2
                          AND project = $3
                        RETURNING *
                        """)
                .execute(Tuple.of(isLoad, group, project));
    }

}
