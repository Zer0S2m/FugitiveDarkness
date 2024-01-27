package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoSearch;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerResponseGitFilter;
import com.zer0s2m.fugitivedarkness.models.GitFilterModel;
import com.zer0s2m.fugitivedarkness.repository.GitFilterRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitFilterRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Request handler for getting all search filters.
 */
final public class ControllerApiGitFilterGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitFilterGet.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Start getting a search filter");
        final GitFilterRepository filterRepository = new GitFilterRepositoryImpl(event.vertx());

        filterRepository
                .findAll()
                .onSuccess(ar -> {
                    final List<ContainerResponseGitFilter> responseGitFilters = new ArrayList<>();
                    final List<GitFilterModel> gitFilterModels = filterRepository.mapTo(ar);
                    final JsonObject object = new JsonObject();

                    gitFilterModels.forEach((gitFilterModel) -> responseGitFilters.add(new ContainerResponseGitFilter(
                            gitFilterModel.getId(),
                            gitFilterModel.getCreatedAt(),
                            gitFilterModel.getTitle(),
                            new JsonObject(gitFilterModel.getFilter())
                                    .mapTo(ContainerGitRepoSearch.class)
                    )));

                    object.put("success", true);
                    object.put("filtersSearch", responseGitFilters);

                    event
                            .response()
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    event.next();
                })
                .onFailure(error -> {
                    filterRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });

        logger.info("Finish getting a search filter");
    }

}
