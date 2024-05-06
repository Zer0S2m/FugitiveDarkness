package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.GitReaderContentFileAdapter;
import com.zer0s2m.fugitivedarkness.provider.git.HelperGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGitUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

/**
 * A class for providing a service for reading file content from a git source.
 */
public class GitReaderContentFileAdapterGit implements GitReaderContentFileAdapter {

    @Override
    public InputStream read(Map<String, Object> properties) throws IOException {
        final String group = (String) properties.get("group");
        final String project = (String) properties.get("project");
        final String file = (String) properties.get("file");

        final Path source = HelperGitRepo.getSourceGitRepository(group, project);

        try (final Repository repository = Git.open(source.toFile())
                .checkout()
                .getRepository()) {
            final ObjectId revision = SearchEngineGitUtils
                    .getRevisionTree(repository, repository.getBranch());

            try (final TreeWalk treeWalk = TreeWalk.forPath(repository, file, revision)) {
                try (final ObjectReader objectReader = repository.newObjectReader()) {
                    ObjectLoader objectLoader = objectReader.open(treeWalk.getObjectId(0));
                    return objectLoader.openStream();
                }
            }
        }
    }

}
