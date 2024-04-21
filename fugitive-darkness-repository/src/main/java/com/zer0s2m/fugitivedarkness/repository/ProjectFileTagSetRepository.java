package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.ProjectFileTagSet;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface ProjectFileTagSetRepository extends Repository<RowSet<Row>, ProjectFileTagSet> {

    Future<RowSet<Row>> setTagByIdToFile(long id, String file);

    Future<RowSet<Row>> unsetTagByIdToFile(long id, String file);

}
