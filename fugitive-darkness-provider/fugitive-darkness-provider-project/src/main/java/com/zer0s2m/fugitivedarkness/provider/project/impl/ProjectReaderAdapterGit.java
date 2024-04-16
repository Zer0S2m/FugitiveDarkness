package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.git.HelperGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGitUtils;
import com.zer0s2m.fugitivedarkness.provider.project.*;
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
import java.util.*;
import java.util.stream.Stream;

/**
 * An adapter for providing information from the git repository.
 */
public class ProjectReaderAdapterGit extends ProjectReaderAdapterAbstract implements ProjectReaderAdapter {

    Logger logger = LoggerFactory.getLogger(ProjectReaderAdapterGit.class);

    private final Collection<FileProject> fileProjects = new ArrayList<>();

    /**
     * Get the sequence in the form of information about file objects for further iteration.
     *
     * @param properties Additional information for starting the adapter.
     * @return A sequence in the form of information about file objects for further iteration.
     * @throws ProjectException                         The general exception is for interacting with projects.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     * @throws IOException                              If an IO error occurred.
     */
    @Override
    public Stream<FileProject> getStream(Map<String, Object> properties) throws ProjectException, IOException {
        checkProperties(properties);

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
                        findDirectoryAndPushFilesProject(it.getEntryPathString());
                        fileProjects.add(new FileProject(
                                it.getEntryPathString(),
                                true,
                                false));
                    }
                }
            }
        }

        return fileProjects.stream();
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

    private void findDirectoryAndPushFilesProject(String entryPath) {
        List<String> entryPathSplit = new ArrayList<>(Arrays.asList(entryPath.split("/")));
        entryPathSplit.removeLast(); // Delete file

        if (!entryPathSplit.isEmpty()) {
            if (entryPathSplit.size() == 1) {
                final FileProject fileProject = new FileProject(
                        String.join("/", entryPathSplit),
                        false,
                        true);

                if (!fileProjects.contains(fileProject)) {
                    fileProjects.add(fileProject);
                }
            } else {
                // For example, if the path is as follows - test_1/test_2/test_file.example
                StringBuilder stringBuilder = new StringBuilder();

                entryPathSplit.forEach(partEntryPathSplit -> {
                    final FileProject fileProject;

                    if (!stringBuilder.isEmpty()) {
                        fileProject = new FileProject(
                                stringBuilder
                                        .append(partEntryPathSplit)
                                        .toString(),
                                false,
                                true);
                    } else {
                        fileProject = new FileProject(
                                partEntryPathSplit,
                                false,
                                true);
                    }

                    if (!fileProjects.contains(fileProject)) {
                        fileProjects.add(fileProject);
                    }

                    stringBuilder
                            .append(partEntryPathSplit)
                            .append("/");
                });
            }
        }
    }

}
