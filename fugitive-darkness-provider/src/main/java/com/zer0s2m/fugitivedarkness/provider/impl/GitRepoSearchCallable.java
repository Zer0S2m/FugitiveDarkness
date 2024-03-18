package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.provider.ContainerInfoSearchGitRepo;
import com.zer0s2m.fugitivedarkness.provider.GitRepoManager;
import com.zer0s2m.fugitivedarkness.provider.GitRepoFilterSearch;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Call the repository search and return the result.
 */
class GitRepoSearchCallable implements Callable<List<ContainerInfoSearchGitRepo>> {

    private final GitRepoFilterSearch filterSearch;

    private final GitRepoManager gitRepoService = new GitRepoManagerImpl();

    public GitRepoSearchCallable(GitRepoFilterSearch filterSearch) {
        this.filterSearch = filterSearch;
    }

    /**
     * Call a search for one repository based on the installed filter {@link GitRepoFilterSearch}.
     *
     * @return Search result in git repository.
     */
    @Override
    public List<ContainerInfoSearchGitRepo> call() {
        return gitRepoService.searchByGrep(filterSearch);
    }

}
