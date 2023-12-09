package com.zer0s2m.fugitivedarkness.repository;

import io.vertx.core.Future;

public interface Repository<T, D> {

    Future<T> findById(int id);

    /**
     * Saves a given entity.
     * @param entity Must not be null.
     * @return The saved entity
     */
    Future<T> save(D entity);

}
