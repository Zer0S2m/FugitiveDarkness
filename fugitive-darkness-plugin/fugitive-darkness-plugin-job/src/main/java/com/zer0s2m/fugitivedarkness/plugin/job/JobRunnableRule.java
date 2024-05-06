package com.zer0s2m.fugitivedarkness.plugin.job;

/**
 * An interface for implementing the launch of rules before launching deferred tasks.
 */
public interface JobRunnableRule {

    /**
     * Add a rule to start a deferred task.
     *
     * @param rule Rule.
     */
    void addRule(Rule rule);

    /**
     * Clear all rules for running a deferred task.
     */
    void clearRule();

    /**
     * Run all validation rules before starting a deferred task.
     *
     * @param type The type of deferred task to run certain rules.
     * @throws JobException               A general exception.
     * @throws JobValidationRuleException Validation was unsuccessful.
     */
    void run(Object type) throws JobException;

}
