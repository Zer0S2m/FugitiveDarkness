package com.zer0s2m.fugitivedarkness.api;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zer0s2m.fugitivedarkness.common.Environment;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class Main {

    static private final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        runMigrate();
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

    private static void runMigrate() {
        Flyway flyway = Flyway
                .configure()
                .dataSource(
                        "jdbc:postgresql://"
                                + Environment.FD_POSTGRES_HOST
                                + ":"
                                + Environment.FD_POSTGRES_PORT
                                + "/"
                                + Environment.FD_POSTGRES_DB,
                        Environment.FD_POSTGRES_USER,
                        Environment.FD_POSTGRES_PASSWORD)
                .load();
        flyway.migrate();
    }

}
