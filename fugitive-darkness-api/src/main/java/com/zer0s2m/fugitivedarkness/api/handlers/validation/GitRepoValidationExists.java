package com.zer0s2m.fugitivedarkness.api.handlers.validation;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Validation handler for checking the repository in the system.
 */
public class GitRepoValidationExists implements Handler<RoutingContext> {

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        final GitRepoRepository repositoryGit = new GitRepoRepositoryImpl(event.vertx());
        final JsonObject requestBody = event
                .body()
                .asJsonObject();

        final long gitRepositoryId = requestBody.getLong("gitRepositoryId");

        repositoryGit
                .existsById(gitRepositoryId)
                .onSuccess((ar) -> {
                    repositoryGit.closeClient();

                    if (!repositoryGit.mapToExistsColumn(ar)) {
                        event.fail(
                                HttpResponseStatus.NOT_FOUND.code(),
                                new NotFoundException("The repository not found in the system"));
                    } else {
                        event.next();
                    }
                });
    }

}