package com.zer0s2m.fugitivedarkness.plugin.job;

/**
 * The basic executor for performing tasks related to git repositories.
 */
public interface GitJobExecutor extends Runnable {

    /**
     * Set the project group.
     *
     * @param group The project group.
     */
    void setGroup(String group);

    /**
     * Set the name of the project.
     *
     * @param project The name of the project.
     */
    void setProject(String project);

}
