package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.Collection;

public interface GitRepoRepository extends Repository<RowSet<Row>, GitRepoModel> {

    /**
     * Deletes an object with the specified group and project name.
     *
     * @param group   Must not be null.
     * @param project Must not be null.
     */
    Future<RowSet<Row>> deleteByGroupAndProject(String group, String project);

    /**
     * Check entry for existence by group and project.
     *
     * @param group   Git repository group.
     * @param project Name of the project.
     * @return Result.
     */
    Future<RowSet<Row>> existsByGroupAndProject(String group, String project);

    /**
     * Check entry for existence by ID.
     *
     * @param id ID repository.
     * @return Result.
     */
    Future<RowSet<Row>> existsById(long id);

    /**
     * Update the "loaded" attribute by group and project.
     *
     * @param group   Must not be null.
     * @param project Must not be null.
     * @param isLoad  Attribute. Must not be null.
     * @return Result.
     */
    Future<RowSet<Row>> updateIsLoadByGroupAndProject(String group, String project, boolean isLoad);

    /**
     * Save all entities to the database.
     *
     * @param entities Entities.
     * @return Result.
     */
    Future<RowSet<Row>> saveAll(Collection<GitRepoModel> entities);

    /**
     * Find all entities by project group and remote host.
     *
     * @param group Project group.
     * @param host Remote host.
     * @return Result.
     */
    Future<RowSet<Row>> findAllByGroupAndHost(String group, String host);

    /**
     * Find an entity by group name and project.
     *
     * @param group Project group.
     * @param project Name of the project.
     * @return Result.
     */
    Future<RowSet<Row>> findByGroupAndProject(String group, String project);
}
