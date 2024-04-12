package com.zer0s2m.fugitivedarkness.api.handlers.validation;

import com.zer0s2m.fugitivedarkness.plugin.job.Cron;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.BodyProcessorException;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class CronValidation implements Handler<RoutingContext> {

    private final String propertyName;

    public CronValidation(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        final JsonObject requestBody = event
                .body()
                .asJsonObject();

        final String property = requestBody.getString(propertyName);
        final Pattern regex = Pattern.compile(Cron.REGEX.value());

        if (!regex.matcher(property).find()) {
            event.fail(
                    HttpResponseStatus.BAD_REQUEST.code(),
                    BodyProcessorException.createValidationError(
                            event.request().getHeader(HttpHeaders.CONTENT_TYPE),
                            new RuntimeException("Invalid cron expression")));
        } else {
            event.next();
        }
    }

}
