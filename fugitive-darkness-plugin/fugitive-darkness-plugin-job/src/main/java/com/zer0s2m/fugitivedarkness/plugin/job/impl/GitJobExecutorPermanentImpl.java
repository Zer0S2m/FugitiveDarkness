package com.zer0s2m.fugitivedarkness.plugin.job.impl;

import com.zer0s2m.fugitivedarkness.plugin.job.*;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoManager;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Service for a scheduled task with the type {@link GitTypeJob#PERMANENT}.
 */
class GitJobExecutorPermanentImpl extends GitJobExecutorAbstract
        implements GitJobExecutorPermanent {

    Logger logger = LoggerFactory.getLogger(GitJobExecutorPermanentImpl.class);

    @Override
    public void run() {
        try {
            logger.info("The beginning of cloning the repository [" + group + ":" + project + "]");

            GitRepoManager
                    .create()
                    .gFetch(group, project);

            logger.info("End of repository cloning [" + group + ":" + project + "]");
        } catch (GitAPIException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
