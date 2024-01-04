package com.zer0s2m.fugitivedarkness.repository;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface Repository<T, D> {

    /**
     * Get an entity by unique ID key.
     *
     * @param id Must not be null.
     * @return Entity.
     */
    Future<T> findById(int id);

    /**
     * Get all entities.
     *
     * @return entities
     */
    Future<T> findAll();

    /**
     * Saves a given entity.
     *
     * @param entity Must not be null.
     * @return The saved entity
     */
    Future<T> save(D entity);

    /**
     * Instantiate a Java object from a {@link JsonObject}.
     *
     * @param rows The execution result of the row set of a query provided.
     * @return Result.
     */
    List<D> mapTo(final T rows);

    boolean mapToExistsColumn(final T rows);

}
