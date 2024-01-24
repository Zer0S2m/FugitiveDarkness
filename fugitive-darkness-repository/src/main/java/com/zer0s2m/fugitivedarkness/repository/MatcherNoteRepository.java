package com.zer0s2m.fugitivedarkness.repository;

import com.zer0s2m.fugitivedarkness.models.MatcherNoteModel;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public interface MatcherNoteRepository extends Repository<RowSet<Row>, MatcherNoteModel> {
}
