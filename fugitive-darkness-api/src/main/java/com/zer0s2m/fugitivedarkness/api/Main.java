package com.zer0s2m.fugitivedarkness.api;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    static private final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx
                .deployVerticle(new FugitiveDarknessApp())
                .onComplete(ar -> {
                    if (!ar.succeeded()) {
                        logger.error(ar.cause().getMessage());
                        vertx.close();
                    }
                });
    }

}
