package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.models.DocxFileModel;
import com.zer0s2m.fugitivedarkness.repository.DocxFileRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.List;
import java.util.stream.StreamSupport;

public class DocxFileRepositoryImpl extends RepositoryImpl implements DocxFileRepository {

    private final Vertx vertx;

    public DocxFileRepositoryImpl(Vertx vertx) {
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
        return null;
    }

    /**
     * Get all entities.
     *
     * @return entities
     */
    @Override
    public Future<RowSet<Row>> findAll() {
        return sqlClient(vertx).query("""
                SELECT "docx_files"."id",
                       "docx_files"."created_at",
                       "docx_files"."path",
                       "docx_files"."title",
                       "docx_files"."origin_title"
                FROM "docx_files";
                """).execute();
    }

    /**
     * Saves a given entity.
     *
     * @param entity Must not be null.
     * @return The saved entity
     */
    @Override
    public Future<RowSet<Row>> save(DocxFileModel entity) {
        return sqlClient(vertx).preparedQuery("""
                    INSERT INTO "docx_files" (path, title, origin_title)
                    VALUES ($1, $2, $3)
                    RETURNING *
                """).execute(Tuple.of(entity.getPath(), entity.getTitle(), entity.getOriginTitle()));
    }

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public List<DocxFileModel> mapTo(RowSet<Row> rows) {
        return StreamSupport.stream(rows.spliterator(), false).map(row -> row.toJson().mapTo(DocxFileModel.class)).toList();
    }

    /**
     * Convert EXISTS request to object.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    @Override
    public boolean mapToExistsColumn(RowSet<Row> rows) {
        return super.mapToExistsColumn(rows);
    }

    /**
     * Check entry for existence by ID.
     *
     * @param id ID docx file.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> existsById(long id) {
        return sqlClient(vertx).preparedQuery("""
                SELECT EXISTS(select id
                              from "docx_files"
                              WHERE "docx_files"."id" = $1)
                """).execute(Tuple.of(id));
    }

    /**
     * Delete an entity from the database by id.
     *
     * @param id ID docs file.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> deleteById(long id) {
        return sqlClient(vertx).preparedQuery("""
                DELETE
                FROM "docx_files"
                WHERE "docx_files".id = $1
                RETURNING "docx_files".*
                """).execute(Tuple.of(id));
    }

    /**
     * Find an entity by a unique identifier ID.
     *
     * @param id ID docs file.
     * @return Result.
     */
    @Override
    public Future<RowSet<Row>> findById(long id) {
        return sqlClient(vertx).preparedQuery("""
                SELECT "docx_files".*
                FROM "docx_files"
                WHERE "docx_files"."id" = $1
                                        """)
                .execute(Tuple.of(id));
    }

}
