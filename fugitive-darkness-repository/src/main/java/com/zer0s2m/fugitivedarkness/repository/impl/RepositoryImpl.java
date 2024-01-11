package com.zer0s2m.fugitivedarkness.repository.impl;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.repository.Repository;
import com.zer0s2m.fugitivedarkness.repository.RepositoryOperationExists;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

/**
 * A closed class to provide {@link Repository} with the necessary
 * functionality to work correctly with the database.
 */
class RepositoryImpl implements RepositoryOperationExists {

    private final PgConnectOptions connectOptions = connectOptions();

    private final PoolOptions poolOptions = poolOptions();

    /**
     * Get configuration for connecting to the database.
     * @return configuration.
     */
    private PgConnectOptions connectOptions() {
        return new PgConnectOptions()
                .setPort(Environment.FD_POSTGRES_PORT)
                .setHost(Environment.FD_POSTGRES_HOST)
                .setDatabase(Environment.FD_POSTGRES_DB)
                .setUser(Environment.FD_POSTGRES_USER)
                .setPassword(Environment.FD_POSTGRES_PASSWORD);
    }

    /**
     * Get configuration for pool.
     * @return configuration
     */
    private PoolOptions poolOptions() {
        return new PoolOptions()
                .setMaxSize(10);
    }

    protected PgConnectOptions getConnectOptions() {
        return connectOptions;
    }

    protected PoolOptions getPoolOptions() {
        return poolOptions;
    }

    /**
     * Get a configured client to interact with the database.
     * @return Client pool.
     */
    protected SqlClient sqlClient(Vertx vertx) {
        return PgBuilder
                .client()
                .with(getPoolOptions())
                .connectingTo(getConnectOptions())
                .using(vertx)
                .build();
    }

}
