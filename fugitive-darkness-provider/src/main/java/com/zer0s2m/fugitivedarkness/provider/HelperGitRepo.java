package com.zer0s2m.fugitivedarkness.provider;

import com.zer0s2m.fugitivedarkness.common.Environment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * Helper for managing git repositories.
 */
public interface HelperGitRepo {

    Pattern REMOTE_HTTPS = Pattern.compile("^https?://[a-zA-Z0-9.\\-_]+/?[a-zA-Z0-9\\-]+/?[a-zA-Z0-9\\-.]+\\.git/?");

    Pattern REMOTE_SSH = Pattern.compile("[a-zA-Z0-9\\-_]+@[a-zA-Z0-9.\\-_]+:[a-zA-Z0-9.\\-_/]+\\.git");

    /**
     * Get information about a git repository from a remote host.
     * @param URI Remote git repository host.
     * @return Git repository information.
     */
    static ContainerInfoRepo getInfoRepo(final String URI) {
        if (REMOTE_HTTPS.matcher(URI).find()) {
            String[] URISplit = URI.split("/");
            return new ContainerInfoRepo(
                    URISplit[URISplit.length - 3],
                    URISplit[URISplit.length - 2],
                    URISplit[URISplit.length - 1].replace(".git", ""),
                    URI,
                    null
            );
        } else if (REMOTE_SSH.matcher(URI).find()) {
            // TODO: create info git repo
        }
        return new ContainerInfoRepo(null, null, null, null, null);
    }

    /**
     * Check the git repository for its existence in the file system.
     * @param group Repository group.
     * @param project The name of the git repository.
     * @return Is there a repository.
     */
    static boolean existsGitRepository(final String group, final String project) {
        final Path source = Path.of(Environment.ROOT_PATH_REPO, group, project);
        return Files.exists(source);
    }

    /**
     * Get the source path of the git repository in the system.
     * @param group Repository group.
     * @param project The name of the git repository.
     * @return Source path.
     */
    static Path getSourceGitRepository(final String group, final String project) {
        return Path.of(Environment.ROOT_PATH_REPO, group, project, ".git");
    }

}
