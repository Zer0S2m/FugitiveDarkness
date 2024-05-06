package com.zer0s2m.fugitivedarkness.plugin.vertx.job;

import io.vertx.core.Vertx;

/**
 * An interface for implementing listening to scheduled tasks.
 */
public interface Listener  {

    /**
     * Run a listening scan from different sources.
     */
    void listen(Vertx vertx);

}
