package com.zer0s2m.fugitivedarkness.api.handlers;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request handler for getting all search filters.
 */
final public class ControllerApiGitFilterGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitFilterGet.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {

    }

}
