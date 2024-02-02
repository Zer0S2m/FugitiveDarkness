package com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.impl;

import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.GitRepoProviderConverter;
import com.zer0s2m.fugitivedarkness.provider.ContainerInfoRepo;
import com.zer0s2m.fugitivedarkness.provider.GitProviderNotFoundException;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderType;
import io.vertx.core.json.JsonObject;

/**
 * Helper that converts the response from providers {@link GitRepoProviderType} into
 * a typical response for the system.
 */
public class GitRepoProviderConverterImpl implements GitRepoProviderConverter {

    private static String hostGithub;

    private static String hostGitlab;

    public GitRepoProviderConverterImpl() {
        hostGithub = "github.com";
        hostGitlab = "gitlab.com";
    }

    /**
     * Convert the response from the repository provider into a familiar object for the system.
     *
     * @param providerType Provider type.
     * @param payload      Response from the repository provider.
     * @return Repository information.
     * @throws GitProviderNotFoundException Git provider not found.
     */
    @Override
    public ContainerInfoRepo convert(final GitRepoProviderType providerType, JsonObject payload)
            throws GitProviderNotFoundException {
        if (providerType.equals(GitRepoProviderType.GITHUB)) {
            return GitRepoProviderGithubConverter.convert(payload);
        } else if (providerType.equals(GitRepoProviderType.GITLAB)) {
            return GitRepoProviderGitlabConverter.convert(payload);
        }

        throw new GitProviderNotFoundException("Converter does not support");
    }

    /**
     * Set provider host.
     *
     * @param host Host.
     */
    @Override
    public void setHost(final GitRepoProviderType providerType, String host)
            throws GitProviderNotFoundException {
        if (providerType.equals(GitRepoProviderType.GITHUB)) {
            GitRepoProviderConverterImpl.hostGithub = host;
        } else if (providerType.equals(GitRepoProviderType.GITLAB)) {
            GitRepoProviderConverterImpl.hostGitlab = host;
        }

        throw new GitProviderNotFoundException("Converter does not support");
    }

    /**
     * Provider {@link GitRepoProviderType#GITHUB} converter.
     */
    private static class GitRepoProviderGithubConverter {

        public static ContainerInfoRepo convert(JsonObject payload) {
            return new ContainerInfoRepo(
                    hostGithub,
                    payload.getJsonObject("owner").getString("login"),
                    payload.getString("name"),
                    payload.getString("clone_url"),
                    null
            );
        }

    }

    /**
     * Provider {@link GitRepoProviderType#GITLAB} converter.
     */
    private static class GitRepoProviderGitlabConverter {

        public static ContainerInfoRepo convert(JsonObject payload) {
            return new ContainerInfoRepo(
                    hostGitlab,
                    payload.getJsonObject("namespace").getString("path"),
                    payload.getString("name"),
                    payload.getString("http_url_to_repo"),
                    null
            );
        }

    }

}
