package com.zer0s2m.fugitivedarkness.provider;

import com.zer0s2m.fugitivedarkness.provider.impl.GitRepoProviderGithub;
import com.zer0s2m.fugitivedarkness.provider.impl.GitRepoProviderGitlab;

/**
 * Basic interface for implementing a service for working with provider git repositories.
 */
public interface GitRepoProvider {

    /**
     * Get provider information to retrieve repositories from an organization.
     *
     * @param org Name of organization.
     * @return Provider information.
     */
    GitRepoProviderInfo getInfoGitRepositoriesForOrg(String org);

    /**
     * Get provider information to get repositories for the user.
     *
     * @param username User login.
     * @return Provider information.
     */
    GitRepoProviderInfo getInfoGitRepositoriesForUser(String username);

    /**
     * Set provider host.
     *
     * @param host Host.
     */
    void setHost(String host);

    /**
     * Get provider host.
     *
     * @return Host.
     */
    String getHost();

    /**
     * Create a provider based on type.
     *
     * @param providerType Provider type.
     * @return Provider
     */
    static GitRepoProvider create(GitRepoProviderType providerType) {
        if (providerType.equals(GitRepoProviderType.GITHUB)) {
            return new GitRepoProviderGithub();
        } else if (providerType.equals(GitRepoProviderType.GITLAB)) {
            return new GitRepoProviderGitlab();
        }

        throw new RuntimeException("There is no provider for " + providerType);
    }

}
