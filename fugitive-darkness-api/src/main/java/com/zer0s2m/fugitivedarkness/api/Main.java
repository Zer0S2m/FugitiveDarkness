package com.zer0s2m.fugitivedarkness.api;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class Main {

    static private final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        registerModules();

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

    private static void registerModules() {
        DatabindCodec
                .mapper()
                .registerModule(new JavaTimeModule())
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
    }

}
