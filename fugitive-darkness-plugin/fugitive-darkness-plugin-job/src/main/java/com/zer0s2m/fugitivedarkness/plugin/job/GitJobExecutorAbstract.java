package com.zer0s2m.fugitivedarkness.plugin.job;

import java.util.Objects;

public abstract class GitJobExecutorAbstract implements GitJobExecutor {

    protected String group;

    protected String project;

    /**
     * Set the project group.
     *
     * @param group The project group.
     */
    @Override
    public void setGroup(String group) {
        Objects.requireNonNull(group);

        this.group = group;
    }

    /**
     * Set the name of the project.
     *
     * @param project The name of the project.
     */
    @Override
    public void setProject(String project) {
        Objects.requireNonNull(project);

        this.project = project;
    }

    /**
     * Check the repository for its existence.
     *
     * @throws JobNotFoundGitRepositoryException The git repository was not found.
     */
    protected abstract void checkExistsGitRepository() throws JobException;

}
