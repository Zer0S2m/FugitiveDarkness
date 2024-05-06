package com.zer0s2m.fugitivedarkness.plugin.job;

/**
 * An exception for not passing the rule for starting a task.
 */
public class JobValidationRuleException extends JobException {

    public JobValidationRuleException(String message) {
        super(message);
    }

}
