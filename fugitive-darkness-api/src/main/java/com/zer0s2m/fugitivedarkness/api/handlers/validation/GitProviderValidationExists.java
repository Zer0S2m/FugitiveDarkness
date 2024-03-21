package com.zer0s2m.fugitivedarkness.api.handlers.validation;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerInfoGitProviderControl;
import com.zer0s2m.fugitivedarkness.repository.GitProviderRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitProviderRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

/**
 *  Validation before deleting a git provider - checks for the existence of an entry in the database.
 */
final public class GitProviderValidationExists implements Handler<RoutingContext> {

    /**
     * Validation for the existence of a record in the database.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        final GitProviderRepository gitProviderRepository = new GitProviderRepositoryImpl(event.vertx());
        final ContainerInfoGitProviderControl containerInfoGitProviderDelete = event
                .body()
                .asJsonObject()
                .mapTo(ContainerInfoGitProviderControl.class);

        gitProviderRepository
                .existsByTypeAndTarget(
                        containerInfoGitProviderDelete.type(),
                        containerInfoGitProviderDelete.target())
                .onSuccess(ar -> {
                    if (!gitProviderRepository.mapToExistsColumn(ar)) {
                        event.fail(
                                HttpResponseStatus.NOT_FOUND.code(),
                                new NotFoundException("Git provider does not exist"));
                    } else {
                        event.next();
                    }

                    gitProviderRepository.closeClient();
                });
    }

}
