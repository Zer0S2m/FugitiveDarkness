package com.zer0s2m.fugitivedarkness.plugin.job;

/**
 * The exception is for the git repository that was not found.
 */
public class JobNotFoundGitRepositoryException extends JobException {

    public JobNotFoundGitRepositoryException(String message) {
        super(message);
    }

}
