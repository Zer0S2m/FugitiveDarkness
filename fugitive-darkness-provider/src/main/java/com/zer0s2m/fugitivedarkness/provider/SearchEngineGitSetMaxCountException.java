package com.zer0s2m.fugitivedarkness.provider;

/**
 * Exception for setting maximum search depth.
 */
public class SearchEngineGitSetMaxCountException extends SearchEngineGitException {

    public static final SearchEngineGitExceptionType TYPE = SearchEngineGitExceptionType.SET;

    public String toString() {
        return String.format("Type [%s] - %s",
                TYPE, super.toString()
        );
    }

}
