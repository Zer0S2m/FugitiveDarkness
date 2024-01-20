package com.zer0s2m.fugitivedarkness.provider;

/**
 * Exception for setting the maximum number of matches in one file.
 */
public class SearchEngineGitSetMaxCountException extends SearchEngineGitException {

    public static final SearchEngineGitExceptionType TYPE = SearchEngineGitExceptionType.SET;

    public String toString() {
        return String.format("Type [%s] - %s",
                TYPE, super.toString()
        );
    }

}
