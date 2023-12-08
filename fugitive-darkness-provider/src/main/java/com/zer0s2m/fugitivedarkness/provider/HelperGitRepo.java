package com.zer0s2m.fugitivedarkness.provider;

import java.util.regex.Pattern;

/**
 * Helper for managing git repositories.
 */
public interface HelperGitRepo {

    Pattern REMOTE_HTTPS = Pattern.compile("^https?://[a-zA-Z0-9.\\-_]+/?[a-zA-Z0-9\\-]+/?[a-zA-Z0-9\\-]+\\.git/?");

    Pattern REMOTE_SSH = Pattern.compile("[a-zA-Z0-9\\-_]+@[a-zA-Z0-9.\\-_]+:[a-zA-Z0-9.\\-_/]+\\.git");

    static ContainerInfoRepo getInfoRepo(final String URI) {
        if (REMOTE_HTTPS.matcher(URI).find()) {
            String[] URISplit = URI.split("/");
            return new ContainerInfoRepo(
                    URISplit[URISplit.length - 3],
                    URISplit[URISplit.length - 2],
                    URISplit[URISplit.length - 1].replace(".git", "")
            );
        } else if (REMOTE_SSH.matcher(URI).find()) {
            System.out.println(2);
            System.out.println(URI);
        }
        return new ContainerInfoRepo(null, null, null);
    }

}
