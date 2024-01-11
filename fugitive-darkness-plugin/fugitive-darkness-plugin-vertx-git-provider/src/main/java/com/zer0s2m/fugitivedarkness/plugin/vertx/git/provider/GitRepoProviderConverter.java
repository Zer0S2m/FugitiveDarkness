package com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider;

import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.impl.GitRepoProviderConverterImpl;
import com.zer0s2m.fugitivedarkness.provider.ContainerInfoRepo;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderType;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for implementing a helper that converts the response from
 * providers {@link GitRepoProviderType} into a typical response for the system.
 */
public interface GitRepoProviderConverter {

    /**
     * Convert the response from the repository provider into a familiar object for the system.
     *
     * @param providerType Provider type.
     * @param payload      Response from the repository provider.
     * @return Repository information.
     */
    ContainerInfoRepo convert(final GitRepoProviderType providerType, JsonObject payload);

    /**
     * Convert the response from the repository provider into a familiar object for the system.
     *
     * @param providerType Provider type.
     * @param payload      Response from the repository provider.
     * @return Repository information.
     */
    default List<ContainerInfoRepo> convert(final GitRepoProviderType providerType, JsonArray payload) {
        List<ContainerInfoRepo> result = new ArrayList<>();
        payload
                .stream()
                .forEach((item) -> result.add(convert(providerType, (JsonObject) item)));
        return result;
    }

    /**
     * Set provider host.
     *
     * @param providerType Provider type.
     * @param host Host.
     */
    void setHost(final GitRepoProviderType providerType, String host);

    static GitRepoProviderConverter create() {
        return new GitRepoProviderConverterImpl();
    }

}
