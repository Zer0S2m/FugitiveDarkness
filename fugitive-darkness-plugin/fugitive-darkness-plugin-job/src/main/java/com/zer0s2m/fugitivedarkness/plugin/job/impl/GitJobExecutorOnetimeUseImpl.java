package com.zer0s2m.fugitivedarkness.plugin.job.impl;

import com.zer0s2m.fugitivedarkness.plugin.job.GitJobExecutorAbstract;
import com.zer0s2m.fugitivedarkness.plugin.job.GitJobExecutorOnetimeUse;
import com.zer0s2m.fugitivedarkness.plugin.job.GitTypeJob;
import com.zer0s2m.fugitivedarkness.plugin.job.JobException;
import com.zer0s2m.fugitivedarkness.plugin.job.JobNotFoundGitRepositoryException;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoManager;
import com.zer0s2m.fugitivedarkness.provider.git.HelperGitRepo;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

/**
 * Service for a scheduled task with the type {@link GitTypeJob#ONETIME_USE}.
 */
class GitJobExecutorOnetimeUseImpl extends GitJobExecutorAbstract
        implements GitJobExecutorOnetimeUse {

    @Override
    public void run() {
        try {
            checkExistsGitRepository();

            GitRepoManager
                    .create()
                    .gFetch(group, project);
        } catch (JobException | IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check the repository for its existence.
     *
     * @throws JobNotFoundGitRepositoryException The git repository was not found.
     */
    @Override
    protected void checkExistsGitRepository() throws JobException {
        if (!HelperGitRepo.existsGitRepository(group, project)) {
            throw new JobNotFoundGitRepositoryException(
                    "The Git repository was not found. ["
                            + HelperGitRepo.getSourceGitRepository(group, project)
                            + "]");
        }
    }

}
