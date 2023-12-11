package com.zer0s2m.fugitivedarkness.common;

/**
 * Environment variables for system performance
 */
final public class Environment {

    /**
     * Root path to the git repository.
     */
    public static final String ROOT_PATH_REPO = getRootPathRepo();

    public static final int FD_POSTGRES_PORT = getFdPostgresPort();

    public static final String FD_POSTGRES_HOST = getFdPostgresHost();

    public static final String FD_POSTGRES_USER = getFdPostgresUser();

    public static final String FD_POSTGRES_DB = getFdPostgresDb();

    public static final String FD_POSTGRES_PASSWORD = getFdPostgresPassword();

    private static String getRootPathRepo() {
        return System.getenv("FD_ROOT_PATH_REPO");
    }

    private static int getFdPostgresPort() {
        return Integer.parseInt(System.getenv("FD_POSTGRES_PORT"));
    }

    private static String getFdPostgresHost() {
        return System.getenv("FD_POSTGRES_HOST");
    }

    private static String getFdPostgresUser() {
        return System.getenv("FD_POSTGRES_USER");
    }

    private static String getFdPostgresDb() {
        return System.getenv("FD_POSTGRES_DB");
    }

    private static String getFdPostgresPassword() {
        return System.getenv("FD_POSTGRES_PASSWORD");
    }

}
