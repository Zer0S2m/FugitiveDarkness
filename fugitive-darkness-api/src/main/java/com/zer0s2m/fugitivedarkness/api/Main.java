package com.zer0s2m.fugitivedarkness.api;

import io.vertx.core.Vertx;

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new FugitiveDarknessApp());
    }

}
