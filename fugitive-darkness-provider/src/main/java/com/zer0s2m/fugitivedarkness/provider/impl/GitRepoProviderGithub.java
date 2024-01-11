package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.provider.GitRepoProvider;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderAbstract;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderType;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Provider for working with a supplier {@link GitRepoProviderType#GITHUB}.
 */
public class GitRepoProviderGithub extends GitRepoProviderAbstract implements GitRepoProvider {

    private final String scheme = "https";

    private final String host = "api.github.com";

    /**
     * Path to get repositories for the specified organization.
     */
    private static final String pathGetRepoForOrg = "/orgs/%s/repos";

    /**
     * Path to get repositories for the specified user.
     */
    private static final String pathGetRepoForUser = "/users/%s/repos";

    private final Map<String, String> headers = new HashMap<>();

    public GitRepoProviderGithub() {
        super(pathGetRepoForOrg, pathGetRepoForUser);

        headers.put("X-GitHub-Api-Version", "2022-11-28");
        headers.put("Accept", "application/vnd.github+json");
    }

    /**
     * Get provider information to retrieve repositories from an organization.
     * <p><a href="https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-organization-repositories">More about</a>.</p>
     *
     * @param org Name of organization.
     * @return Provider information.
     */
    @Override
    public GitRepoProviderInfo getInfoGitRepositoriesForOrg(final String org) {
        return new GitRepoProviderInfo(
                host,
                scheme,
                collectPathGetGitRepositoriesForOrg(org),
                headers
        );
    }

    /**
     * Get provider information to get repositories for the user.
     * <p><a href="https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user">More about</a>.</p>
     *
     * @param username User login.
     * @return Provider information.
     */
    @Override
    public GitRepoProviderInfo getInfoGitRepositoriesForUser(final String username) {
        return new GitRepoProviderInfo(
                host,
                scheme,
                collectPathGetGitRepositoriesForUser(username),
                headers
        );
    }

    /**
     * Set provider host.
     *
     * @param host Host.
     */
    @Override
    public void setHost(String host) {
        throw new RuntimeException("It is not possible to install a host for this provider [github].");
    }

    /**
     * Get provider host.
     *
     * @return Host.
     */
    @Override
    public String getHost() {
        return host;
    }

}
