package com.zer0s2m.fugitivedarkness.common;

/**
 * Environment variables for system performance
 */
final public class Environment {

    /**
     * Root path to the git repository.
     */
    public static final String ROOT_PATH_REPO = getRootPathRepo();

    private static String getRootPathRepo() {
        return System.getenv("FD_ROOT_PATH_REPO");
    }

}
