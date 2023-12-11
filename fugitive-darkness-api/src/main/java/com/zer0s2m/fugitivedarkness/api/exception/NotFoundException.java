package com.zer0s2m.fugitivedarkness.api.exception;

import io.vertx.core.VertxException;
import io.vertx.core.json.JsonObject;

/**
 * Throws an exception about an object not found on the system.
 */
public class NotFoundException extends VertxException {

    public NotFoundException(String message) {
        super("[Not Found] " + message);
    }

    /**
     * Returns a Json representation of the exception
     *
     * @return Error information.
     */
    public JsonObject toJson() {
        JsonObject res = new JsonObject()
                .put("type", this.getClass().getSimpleName())
                .put("message", this.getMessage());
        if (this.getCause() != null) {
            res
                    .put("causeType", this.getCause().getClass().getSimpleName())
                    .put("causeMessage", this.getCause().getMessage());
        }
        return res;
    }

}
