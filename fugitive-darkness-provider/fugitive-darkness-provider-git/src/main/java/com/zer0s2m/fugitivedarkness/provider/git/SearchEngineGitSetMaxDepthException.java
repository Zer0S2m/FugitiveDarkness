package com.zer0s2m.fugitivedarkness.provider.git;

/**
 * Exception for setting maximum search depth.
 */
public class SearchEngineGitSetMaxDepthException extends SearchEngineGitException {

    public static final SearchEngineGitExceptionType TYPE = SearchEngineGitExceptionType.SET;

    public String toString() {
        return String.format("Type [%s] - %s",
                TYPE, super.toString()
        );
    }

}
