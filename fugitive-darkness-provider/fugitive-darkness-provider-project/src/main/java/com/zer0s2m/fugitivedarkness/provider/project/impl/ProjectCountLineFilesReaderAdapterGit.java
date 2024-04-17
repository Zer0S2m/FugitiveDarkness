package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.git.HelperGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGitUtils;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectCountLineFilesReaderAdapter;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectCountLineFilesReaderAdapterAbstract;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectException;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectMissingPropertiesAdapterException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * An adapter for providing a reader from the git repository source.
 */
public class ProjectCountLineFilesReaderAdapterGit extends ProjectCountLineFilesReaderAdapterAbstract
        implements ProjectCountLineFilesReaderAdapter {

    Logger logger = LoggerFactory.getLogger(ProjectCountLineFilesReaderAdapterGit.class);

    /**
     * Get a reader to crawl the file.
     *
     * @param properties Additional information for starting the adapter.
     * @return Reader.
     * @throws ProjectException                         The general exception is for interacting with projects.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     * @throws IOException                              If an IO error occurred.
     * @throws FileNotFoundException                    No file was found.
     */
    @Override
    public Reader getReader(Map<String, Object> properties) throws ProjectException, IOException {
        checkProperties(properties);

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
                    try (final InputStream stream = objectLoader.openStream()) {
                        return new InputStreamReader(stream);
                    }
                }
            }
        }
    }

    /**
     * Check all available information to start the adapter and issue an exception
     * if the necessary information is not provided.
     *
     * @param properties Additional information.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     */
    @Override
    protected void checkProperties(Map<String, Object> properties) throws ProjectMissingPropertiesAdapterException {
        Collection<String> missingProperties = new ArrayList<>();

        if (!properties.containsKey("group")) {
            missingProperties.add("group");
        }
        if (!properties.containsKey("project")) {
            missingProperties.add("project");
        }
        if (!properties.containsKey("file")) {
            missingProperties.add("file");
        }

        if (!missingProperties.isEmpty()) {
            String mgs = "There are no required parameters to start the adapter " + missingProperties;
            logger.debug(mgs);
            throw new ProjectMissingPropertiesAdapterException(mgs);
        }
    }

}
