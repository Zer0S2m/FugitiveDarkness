package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.ProjectFileColorSet;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface ProjectFileColorSetRepository extends Repository<RowSet<Row>, ProjectFileColorSet> {

    Future<RowSet<Row>> setColorByIdToFile(long id, String file);

    Future<RowSet<Row>> unsetColorByIdToFile(long id, String file);

}
