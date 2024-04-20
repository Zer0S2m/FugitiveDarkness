package com.zer0s2m.fugitivedarkness.api.handlers;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A request handler for creating tags for file objects in the project.
 */
public final class ControllerApiProjectFilesTagCreate implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesTagCreate.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Start code");

        //

        logger.info("End code");

        event.next();
    }

}
