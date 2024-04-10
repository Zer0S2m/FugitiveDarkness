package com.zer0s2m.fugitivedarkness.plugin.job.impl;

import com.zer0s2m.fugitivedarkness.plugin.job.*;

/**
 * A manager for running scheduled git repository tasks.
 */
public class GitJobManagerImpl implements GitJobManager {

    /**
     * To call a scheduled task with the type {@link GitTypeJob}.
     *
     * @param group   The git repository group.
     * @param project The name of the git repository.
     * @param typeJob The type of task.
     * @throws JobNotFoundExecutorException The performer for the task has not been found.
     */
    @Override
    public void call(String group, String project, GitTypeJob typeJob) throws JobException {
        if (typeJob.equals(GitTypeJob.ONETIME_USE)) {
            final GitJobExecutor gitJobExecutor = new GitJobExecutorOnetimeUseImpl();
            gitJobExecutor.setGroup(group);
            gitJobExecutor.setProject(project);

            new Thread(gitJobExecutor, "jobs.git-onetime-use.fetch-repository").start();
        } else if (typeJob.equals(GitTypeJob.PERMANENT)) {
            final GitJobExecutor gitJobExecutor = new GitJobExecutorPermanentImpl();
            gitJobExecutor.setGroup(group);
            gitJobExecutor.setProject(project);

            new Thread(gitJobExecutor, "jobs.git-permanent.fetch-repository").start();
        }

        throw new JobNotFoundExecutorException("The task executor was not found.");
    }

}
