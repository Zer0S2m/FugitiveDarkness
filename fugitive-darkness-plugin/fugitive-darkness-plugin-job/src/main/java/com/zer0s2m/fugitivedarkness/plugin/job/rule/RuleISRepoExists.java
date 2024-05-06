package com.zer0s2m.fugitivedarkness.plugin.job.rule;

import com.zer0s2m.fugitivedarkness.plugin.job.*;
import com.zer0s2m.fugitivedarkness.provider.git.HelperGitRepo;

import java.util.Map;

/**
 * A rule for checking for the existence of a repository.
 */
public final class RuleISRepoExists implements Rule {

    private Object typeTask;

    /**
     * Set a specific type of task to run the rules before completing this task.
     * <p>Supported types of deferred tasks:</p>
     * <ul>
     *     <li>{@link GitTypeJob#PERMANENT}</li>
     *     <li>{@link GitTypeJob#ONETIME_USE}</li>
     * </ul>
     * Embedding is used in the following functionality: {@link JobRunnableRule#run(Object)}.
     *
     * @param typeTask Type of deferred task.
     */
    @Override
    public void setTypeTask(Object typeTask) {
        this.typeTask = typeTask;
    }

    /**
     * Get the type of deferred task to run certain rules.
     *
     * @return Type of deferred task.
     */
    @Override
    public Object getTypeTask() {
        return typeTask;
    }

    /**
     * Run a rule for a scheduled task.
     *
     * @param properties Additional information for validation.
     * @throws JobException               A general exception.
     * @throws JobValidationRuleException Validation was unsuccessful.
     */
    @Override
    public void validation(Map<String, Object> properties) throws JobException {
        if (properties.containsKey("group") && properties.containsKey("project")) {
            String group = (String) properties.get("group");
            String project = (String) properties.get("project");

            if (!HelperGitRepo.existsGitRepository(group, project)) {
                throw new JobValidationRuleException("It is impossible to start a deferred " +
                        "task - there is no git repository ["
                        + HelperGitRepo.getSourceGitRepository(group, project)
                        + "]");
            }
        }
    }

}
