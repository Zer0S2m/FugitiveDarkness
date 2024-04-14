package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.git.HelperGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGitUtils;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectException;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectMissingPropertiesAdapterException;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectReaderAdapter;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectReaderAdapterAbstract;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * An adapter for providing information from the git repository.
 */
public class ProjectReaderAdapterGit extends ProjectReaderAdapterAbstract implements ProjectReaderAdapter {

    Logger logger = LoggerFactory.getLogger(ProjectReaderAdapterGit.class);

    /**
     * Get a sequence in the form of file names for further iteration.
     *
     * @param properties Additional information for starting the adapter.
     * @return A sequence in the form of file names for further iteration.
     * @throws ProjectException                         The general exception is for interacting with projects.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     * @throws IOException                              If an IO error occurred.
     */
    @Override
    public Stream<String> getStream(Map<String, Object> properties) throws ProjectException, IOException {
        checkProperties(properties);

        final Collection<String> paths = new ArrayList<>();
        final String group = (String) properties.get("group");
        final String project = (String) properties.get("project");

        final Repository repository = Git
                .open(HelperGitRepo.cleanPathForGitRepo(
                        HelperGitRepo.getSourceGitRepository(group, project)
                ).toFile())
                .checkout()
                .getRepository();

        try (final ObjectReader objectReader = repository.newObjectReader()) {
            ObjectId commitId = SearchEngineGitUtils.getRevision(repository);

            try (final RevWalk revWalk = new RevWalk(objectReader)) {
                try (final TreeWalk treeWalk = new TreeWalk(objectReader)) {
                    RevCommit commit = revWalk.parseCommit(commitId);
                    CanonicalTreeParser treeParser = new CanonicalTreeParser();
                    treeParser.reset(objectReader, commit.getTree());

                    int treeIndex = treeWalk.addTree(treeParser);
                    treeWalk.setRecursive(true);

                    while (treeWalk.next()) {
                        AbstractTreeIterator it = treeWalk.getTree(treeIndex, AbstractTreeIterator.class);
                        paths.add(it.getEntryPathString());
                    }
                }
            }
        }

        return paths.stream();
    }

    @Override
    protected void checkProperties(Map<String, Object> properties) throws ProjectMissingPropertiesAdapterException {
        Collection<String> missingProperties = new ArrayList<>();

        if (!properties.containsKey("group")) {
            missingProperties.add("group");
        }
        if (!properties.containsKey("project")) {
            missingProperties.add("project");
        }

        if (!missingProperties.isEmpty()) {
            String mgs = "There are no required parameters to start the adapter " + missingProperties;
            logger.debug(mgs);
            throw new ProjectMissingPropertiesAdapterException(mgs);
        }
    }

}
