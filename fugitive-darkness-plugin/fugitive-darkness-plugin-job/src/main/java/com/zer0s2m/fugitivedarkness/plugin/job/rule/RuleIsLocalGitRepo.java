package com.zer0s2m.fugitivedarkness.plugin.job.rule;

import com.zer0s2m.fugitivedarkness.plugin.job.GitTypeJob;
import com.zer0s2m.fugitivedarkness.plugin.job.JobException;
import com.zer0s2m.fugitivedarkness.plugin.job.JobRunnableRule;
import com.zer0s2m.fugitivedarkness.plugin.job.JobValidationRuleException;
import com.zer0s2m.fugitivedarkness.plugin.job.Rule;

import java.util.Map;

/**
 * The rule for checking that the git repository should not be local.
 */
public final class RuleIsLocalGitRepo implements Rule {

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
        if (properties.containsKey("isLocal")) {
            Boolean isLocal = (Boolean) properties.get("isLocal");
            if (isLocal) {
                throw new JobValidationRuleException("It is not possible to start a deferred task because " +
                        "the git repository is local.");
            }
        }
    }

}
