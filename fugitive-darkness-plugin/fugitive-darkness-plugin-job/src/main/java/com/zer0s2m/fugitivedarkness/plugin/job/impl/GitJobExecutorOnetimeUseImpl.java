package com.zer0s2m.fugitivedarkness.plugin.job.impl;

import com.zer0s2m.fugitivedarkness.plugin.job.GitJobExecutorAbstract;
import com.zer0s2m.fugitivedarkness.plugin.job.GitJobExecutorOnetimeUse;
import com.zer0s2m.fugitivedarkness.plugin.job.GitTypeJob;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoManager;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Service for a scheduled task with the type {@link GitTypeJob#ONETIME_USE}.
 */
class GitJobExecutorOnetimeUseImpl extends GitJobExecutorAbstract
        implements GitJobExecutorOnetimeUse {

    Logger logger = LoggerFactory.getLogger(GitJobExecutorOnetimeUseImpl.class);

    @Override
    public void run() {
        try {
            logger.info("The beginning of cloning the repository [" + group + ":" + project + "]");

            GitRepoManager
                    .create()
                    .gFetch(group, project);

            logger.info("End of repository cloning [" + group + ":" + project + "]");
        } catch (IOException | GitAPIException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
