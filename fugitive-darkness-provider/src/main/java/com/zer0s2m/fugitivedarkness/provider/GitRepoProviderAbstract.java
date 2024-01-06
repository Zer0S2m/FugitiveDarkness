package com.zer0s2m.fugitivedarkness.provider;

public abstract class GitRepoProviderAbstract {

    /**
     * Path to get repositories for the specified organization.
     */
    private final String pathGetRepoForOrg;

    /**
     * Path to get repositories for the specified user.
     */
    private final String pathGetRepoForUser;

    public GitRepoProviderAbstract(String pathGetRepoForOrg, String pathGetRepoForUser) {
        this.pathGetRepoForOrg = pathGetRepoForOrg;
        this.pathGetRepoForUser = pathGetRepoForUser;
    }

    /**
     * Collect the path to get git repositories for the organization.
     *
     * @param org Name of organization.
     * @return The assembled path.
     */
    protected String collectPathGetGitRepositoriesForOrg(final String org) {
        return String.format(pathGetRepoForOrg, org.trim());
    }

    /**
     * Collect the path to the git repositories for the user's organization.
     *
     * @param username User login.
     * @return The assembled path.
     */
    protected String collectPathGetGitRepositoriesForUser(final String username) {
        return String.format(pathGetRepoForUser, username.trim());
    }

}
