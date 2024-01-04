package com.zer0s2m.fugitivedarkness.api.exception;

import io.vertx.core.VertxException;
import io.vertx.core.json.JsonObject;

/**
 * Exception to check for the existence of an object in the system.
 */
public class ObjectISExistsInSystemException extends VertxException {

    public ObjectISExistsInSystemException(String message) {
        super("[The object exists in the system] " + message);
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
