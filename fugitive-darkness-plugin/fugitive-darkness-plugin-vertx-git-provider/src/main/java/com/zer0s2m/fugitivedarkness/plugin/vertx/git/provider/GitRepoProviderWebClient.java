package com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider;

import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.impl.GitRepoProviderWebClientImpl;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoProviderInfo;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoProviderType;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.HttpResponse;

/**
 * Client for communicating with providers {@link GitRepoProviderType} for git repositories.
 */
public interface GitRepoProviderWebClient {

    /**
     * Get git repositories from a provider based on its type {@link GitRepoProviderType}.
     *
     * @param infoProvider Provider information.
     * @return Result.
     */
    Future<HttpResponse<JsonArray>> getGitRepositories(final GitRepoProviderInfo infoProvider);

    static GitRepoProviderWebClient create(Vertx vertx) {
        return new GitRepoProviderWebClientImpl(vertx);
    }

}
