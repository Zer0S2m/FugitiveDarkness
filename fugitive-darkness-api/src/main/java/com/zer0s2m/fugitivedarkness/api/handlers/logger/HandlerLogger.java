package com.zer0s2m.fugitivedarkness.api.handlers.logger;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class HandlerLogger {

    static public final Logger logger = LoggerFactory.getLogger("http");

    private static final String ANSI_RESET = "\u001b[0m";

    private static final String ANSI_BACK_BLUE = "\u001b[44m";

    private static final String ANSI_BACK_GREEN = "\u001b[42m";

    private static final String ANSI_BACK_YELLOW = "\u001b[43m";

    private static final String ANSI_BACK_RED = "\u001b[41m";

    public static String getMsgRequest(String method) {
        String msg = "%s" + method + ANSI_RESET;

        if (Objects.equals(method, "GET")) {
            msg = String.format(msg, ANSI_BACK_BLUE);
        } else if (Objects.equals(method, "POST")) {
            msg = String.format(msg, ANSI_BACK_GREEN);
        } else if (Objects.equals(method, "PUT")) {
            msg = String.format(msg, ANSI_BACK_YELLOW);
        } else if (Objects.equals(method, "DELETE")) {
            msg = String.format(msg, ANSI_BACK_RED);
        }

        return msg;
    }

    public static String getMsgResponse(int statusCode) {
        String msg = "%s" + statusCode + ANSI_RESET;

        if (statusCode >= 200 && statusCode < 300) {
            msg = String.format(msg, ANSI_BACK_BLUE);
        } else if (statusCode >= 300 && statusCode < 500) {
            msg = String.format(msg, ANSI_BACK_YELLOW);
        } else if (statusCode >= 500) {
            msg = String.format(msg, ANSI_BACK_RED);
        }

        return msg;
    }

    public static class HandlerLoggerRequest implements Handler<RoutingContext> {

        /**
         * Something has happened, so handle it.
         *
         * @param event the event to handle
         */
        @Override
        public void handle(@NotNull RoutingContext event) {
            logger.info("request  [{}] {}",
                    getMsgRequest(event.request().method().toString()),
                    event.request().path());
            event.next();
        }

    }

    public static class HandlerLoggerResponse implements Handler<RoutingContext> {

        /**
         * Something has happened, so handle it.
         *
         * @param event the event to handle
         */
        @Override
        public void handle(@NotNull RoutingContext event) {
            logger.info("response [{}] [{}] {}",
                    getMsgRequest(event.request().method().toString()),
                    getMsgResponse(event.response().getStatusCode()),
                    event.request().path());
            event.response().end();
        }

    }

}
