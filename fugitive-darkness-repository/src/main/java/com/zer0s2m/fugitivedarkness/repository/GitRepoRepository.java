package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface GitRepoRepository extends Repository<RowSet<Row>, GitRepoModel> {

    /**
     * Deletes an object with the specified group and project name.
     *
     * @param group   Must not be null.
     * @param project Must not be null.
     */
    void deleteByGroupAndProject(String group, String project);

    /**
     * Check entry for existence by group and project.
     *
     * @param group   Git repository group.
     * @param project Name of the project.
     * @return Result.
     */
    Future<RowSet<Row>> existsByGroupAndProject(String group, String project);

    /**
     * Update the "loaded" attribute by group and project.
     *
     * @param group   Must not be null.
     * @param project Must not be null.
     * @param isLoad  Attribute. Must not be null.
     * @return Result.
     */
    Future<RowSet<Row>> updateIsLoadByGroupAndProject(String group, String project, boolean isLoad);

}
