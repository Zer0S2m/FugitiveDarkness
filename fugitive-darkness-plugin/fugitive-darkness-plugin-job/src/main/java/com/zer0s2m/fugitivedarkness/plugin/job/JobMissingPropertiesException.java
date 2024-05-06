package com.zer0s2m.fugitivedarkness.plugin.job;

/**
 * Exception for missing properties to run a task.
 */
public class JobMissingPropertiesException extends JobException {

    public JobMissingPropertiesException(String message) {
        super(message);
    }

}
