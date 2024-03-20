package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.DocxFileModel;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface DocxFileRepository extends Repository<RowSet<Row>, DocxFileModel> {
}
