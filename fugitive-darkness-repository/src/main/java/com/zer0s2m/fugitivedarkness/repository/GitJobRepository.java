package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.GitJobModel;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface GitJobRepository extends Repository<RowSet<Row>, GitJobModel> {
}
