package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoSearchGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoManager;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoFilterSearch;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Call the repository search and return the result.
 */
class GitRepoSearchJGitCallable implements Callable<List<ContainerInfoSearchGitRepo>> {

    private final GitRepoFilterSearch filterSearch;

    private final GitRepoManager gitRepoService = new GitRepoManagerImpl();

    public GitRepoSearchJGitCallable(GitRepoFilterSearch filterSearch) {
        this.filterSearch = filterSearch;
    }

    /**
     * Call a search for one repository based on the installed filter {@link GitRepoFilterSearch}.
     *
     * @return Search result in git repository.
     */
    @Override
    public List<ContainerInfoSearchGitRepo> call() {
        return gitRepoService.searchByGrep_jgit(filterSearch);
    }

}
