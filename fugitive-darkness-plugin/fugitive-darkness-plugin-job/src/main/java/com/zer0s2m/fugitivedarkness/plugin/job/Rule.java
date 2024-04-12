package com.zer0s2m.fugitivedarkness.plugin.job;

import java.util.Map;

/**
 * An interface for implementing validation before completing a scheduled task.
 */
public interface Rule {

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
    void setTypeTask(Object typeTask);

    /**
     * Get the type of deferred task to run certain rules.
     *
     * @return Type of deferred task.
     */
    Object getTypeTask();

    /**
     * Run a rule for a scheduled task.
     *
     * @param properties Additional information for validation.
     * @throws JobException               A general exception.
     * @throws JobValidationRuleException Validation was unsuccessful.
     */
    void validation(Map<String, Object> properties) throws JobException;

}
