package com.zer0s2m.fugitivedarkness.common;

import java.util.Arrays;
import java.util.List;

/**
 * Environment variables for system performance
 */
final public class Environment {

    /**
     * Root path to the git repository.
     */
    public static final String ROOT_PATH_REPO = getRootPathRepo();

    /**
     * Root path to the docx files.
     */
    public static final String ROOT_PATH_DOCX = getRootPathDocx();

    public static final int FD_POSTGRES_PORT = getFdPostgresPort();

    public static final String FD_POSTGRES_HOST = getFdPostgresHost();

    public static final String FD_POSTGRES_USER = getFdPostgresUser();

    public static final String FD_POSTGRES_DB = getFdPostgresDb();

    public static final String FD_POSTGRES_PASSWORD = getFdPostgresPassword();

    public static final List<String> FD_ALLOW_ORIGIN = getFdAllowOrigin();

    private static String getRootPathRepo() {
        return System.getenv("FD_ROOT_PATH_REPO");
    }

    private static String getRootPathDocx() {
        return System.getenv("FD_ROOT_PATH_DOCX");
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

    private static List<String> getFdAllowOrigin() {
        return Arrays.asList(System.getenv("FD_ALLOW_ORIGIN").split(","));
    }

}
