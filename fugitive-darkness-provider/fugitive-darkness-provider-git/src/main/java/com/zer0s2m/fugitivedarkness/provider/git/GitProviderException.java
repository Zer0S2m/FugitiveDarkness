package com.zer0s2m.fugitivedarkness.provider.git;

/**
 * Exceptions for git providers.
 */
public abstract class GitProviderException extends Exception {

    public GitProviderException(String message) {
        super(message);
    }

}
