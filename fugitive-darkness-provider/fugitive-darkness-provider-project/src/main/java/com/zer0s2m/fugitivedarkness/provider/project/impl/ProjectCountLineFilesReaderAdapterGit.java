package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.git.HelperGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGitUtils;
import com.zer0s2m.fugitivedarkness.provider.project.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
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
     * Get readers to crawl files.
     *
     * @param properties     Additional information for starting the adapter.
     * @param typeFileObject Type of file object.
     * @return Reader.
     * @throws ProjectException                         The general exception is for interacting with projects.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     * @throws IOException                              If an IO error occurred.
     * @throws FileNotFoundException                    No file was found.
     */
    @Override
    public Iterable<ContainerInfoReader> getReader(Map<String, Object> properties, TypeFileObject typeFileObject)
            throws ProjectException, IOException {
        checkProperties(properties);

        final Collection<ContainerInfoReader> readers = new ArrayList<>();

        final String group = (String) properties.get("group");
        final String project = (String) properties.get("project");
        final String file = (String) properties.get("file");

        final Path source = HelperGitRepo.getSourceGitRepository(group, project);

        try (final Repository repository = Git.open(source.toFile())
                .checkout()
                .getRepository()) {
            final ObjectId lastCommit = SearchEngineGitUtils.getRevision(repository);
            RevTree tree;
            RevCommit commit;
            try (RevWalk revWalk = new RevWalk(repository)) {
                commit = revWalk.parseCommit(lastCommit);
                tree = commit.getTree();
            }

            try (final ObjectReader objectReader = repository.newObjectReader()) {
                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    CanonicalTreeParser treeParser = new CanonicalTreeParser();
                    treeParser.reset(repository.newObjectReader(), commit.getTree());
                    int treeIndex = treeWalk.addTree(treeParser);

                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(true);
                    treeWalk.setFilter(PathFilter.create(file));

                    while (treeWalk.next()) {
                        AbstractTreeIterator it = treeWalk.getTree(treeIndex, AbstractTreeIterator.class);
                        ObjectId objectId = it.getEntryObjectId();
                        ObjectLoader objectLoader = objectReader.open(objectId);

                        readers.add(new ContainerInfoReader(
                                it.getEntryPathString(),
                                new InputStreamReader(objectLoader.openStream())
                        ));
                    }
                }
            }
        }

        return readers;
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
