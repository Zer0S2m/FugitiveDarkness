package com.zer0s2m.fugitivedarkness.plugin.job;

/**
 * The general exception is for the scheduled workers system.
 */
public abstract class JobException extends Exception {

    public JobException(String message) {
        super(message);
    }

}
