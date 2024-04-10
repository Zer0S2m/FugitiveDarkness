package com.zer0s2m.fugitivedarkness.plugin.job.impl;

import com.zer0s2m.fugitivedarkness.plugin.job.*;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoManager;
import com.zer0s2m.fugitivedarkness.provider.git.HelperGitRepo;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

/**
 * Service for a scheduled task with the type {@link GitTypeJob#PERMANENT}.
 */
class GitJobExecutorPermanentImpl extends GitJobExecutorAbstract
        implements GitJobExecutorPermanent {

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
    protected void checkExistsGitRepository() throws JobException {
        if (!HelperGitRepo.existsGitRepository(group, project)) {
            throw new JobNotFoundGitRepositoryException(
                    "The Git repository was not found. ["
                            + HelperGitRepo.getSourceGitRepository(group, project)
                            + "]");
        }
    }

}
