package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.provider.GitRepoProvider;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderAbstract;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderInfo;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderType;

import java.util.HashMap;

/**
 * Provider for working with a supplier {@link GitRepoProviderType#GITLAB}.
 */
public class GitRepoProviderGitlab extends GitRepoProviderAbstract implements GitRepoProvider {

    private final String scheme = "https";

    private String host;

    /**
     * Path to get repositories for the specified organization.
     */
    private final static String PATH_GET_REPO_FOR_ORG = "/api/v4/groups/%s/projects";

    /**
     * Path to get repositories for the specified user.
     */
    private final static String PATH_GET_REPO_FOR_USER = "/api/v4/users/%s/projects";

    public GitRepoProviderGitlab() {
        super(PATH_GET_REPO_FOR_ORG, PATH_GET_REPO_FOR_USER);
        this.host = "gitlab.com"; // Default
    }

    /**
     * Get provider information to retrieve repositories from an organization.
     * <p><a href="https://docs.gitlab.com/ee/api/groups.html#list-a-groups-projects">More about</a>.</p>
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
                new HashMap<>()
        );
    }

    /**
     * Get provider information to get repositories for the user.
     * <p><a href="https://docs.gitlab.com/ee/api/projects.html#list-user-projects">More about</a>.</p>
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
                new HashMap<>()
        );
    }

    /**
     * Set provider host.
     *
     * @param host Host.
     */
    @Override
    public void setHost(String host) {
        this.host = host;
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
