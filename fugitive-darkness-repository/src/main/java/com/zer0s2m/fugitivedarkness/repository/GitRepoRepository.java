package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface GitRepoRepository extends Repository<RowSet<Row>, GitRepoModel> {

    /**
     *
     * @param group Must not be null.
     * @param project Must not be null.
     */
    void deleteByGroupAndProject(String group, String project);

}
