package com.zer0s2m.fugitivedarkness.plugin.job;

/**
 * An exception is for the executor of the task type that was not found for execution.
 */
public class JobNotFoundExecutorException extends JobException {

    public JobNotFoundExecutorException(String message) {
        super(message);
    }

}
