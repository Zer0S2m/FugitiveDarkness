package com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.impl;

import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.GitRepoProviderWebClient;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderInfo;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderType;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client for communicating with providers {@link GitRepoProviderType} for git repositories.
 */
public class GitRepoProviderWebClientImpl implements GitRepoProviderWebClient {

    static private final Logger logger = LoggerFactory.getLogger(GitRepoProviderWebClientImpl.class);

    private final Vertx vertx;

    public GitRepoProviderWebClientImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * Get git repositories from a provider based on its type {@link GitRepoProviderType}.
     *
     * @param infoProvider Provider information.
     * @return Result.
     */
    @Override
    public Future<HttpResponse<JsonArray>> getGitRepositories(final GitRepoProviderInfo infoProvider) {
        final WebClient client = WebClient.create(vertx);

        logger.info(String.format(
                "Request for data: host [%s] path [%s]",
                infoProvider.host(), infoProvider.path()));

        return client
                .get(infoProvider.host(), infoProvider.path())
                .putHeaders(MultiMap
                        .caseInsensitiveMultiMap()
                        .addAll(infoProvider.headers()))
                .as(BodyCodec.jsonArray())
                .idleTimeout(5000)
                .send();
    }

}
