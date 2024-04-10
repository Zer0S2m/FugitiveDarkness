package com.zer0s2m.fugitivedarkness.plugin.job;

/**
 * A manager for running scheduled git repository tasks.
 */
public interface GitJobManager {

    /**
     * To call a scheduled task with the type {@link GitTypeJob}.
     *
     * @param group   The git repository group.
     * @param project The name of the git repository.
     * @param typeJob The type of task.
     * @throws JobNotFoundExecutorException The performer for the task has not been found.
     */
    void call(String group, String project, GitTypeJob typeJob) throws JobException;

}
