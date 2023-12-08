package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.provider.ContainerInfoRepo;
import com.zer0s2m.fugitivedarkness.provider.GitRepo;
import com.zer0s2m.fugitivedarkness.provider.HelperGitRepo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;

/**
 * Service for interacting with git repositories
 */
public class GitRepoImpl implements GitRepo {

    Logger logger = LoggerFactory.getLogger(GitRepo.class);

    /**
     * Clone a git repository from a remote host into a set path environment variable {@link Environment#ROOT_PATH_REPO}.
     *
     * @param URI Remote git repository host.
     * @return Git repository information.
     * @throws GitAPIException The exception is caused by the internal functionality of managing git repositories.
     */
    @Override
    public ContainerInfoRepo gClone(String URI) throws GitAPIException {
        final ContainerInfoRepo infoRepo = HelperGitRepo.getInfoRepo(URI);
        final File source = Path.of(
                Environment.ROOT_PATH_REPO,
                infoRepo.group(),
                infoRepo.project()
        ).toFile();

        logger.info("Start cloning the repository: " + source.getPath());

        Git.cloneRepository()
                .setURI(URI)
                .setDirectory(source)
                .setCloneAllBranches(true)
                .setCloneSubmodules(true)
                .setNoCheckout(true)
                .call()
                .close();

        logger.info("Finish cloning the repository: " + source.getPath());

        return infoRepo;
    }

    @Override
    public void gDelete() {

    }

}
