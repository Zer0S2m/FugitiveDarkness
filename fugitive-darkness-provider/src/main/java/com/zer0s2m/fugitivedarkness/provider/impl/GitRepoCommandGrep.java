package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.provider.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for providing search in git repositories. Similar to the
 * <a href="https://git-scm.com/docs/git-grep">grep</a> command in git.
 */
class GitRepoCommandGrep extends SearchEngineGitGrepAbstract implements SearchEngineGitGrep {

    /**
     * Helper for finding past lines of code.
     */
    private final GitRepoCommandGrepUtils.GitRepoCommandGrepUtilPreviewCode utilPreviewCode =
            new GitRepoCommandGrepUtils.GitRepoCommandGrepUtilPreviewCode();

    /**
     * @param pattern              A pattern for finding matches in files.
     * @param source               Source path to the git repository.
     * @param containerGitRepoMeta Additional Information.
     * @throws IOException If an IO error occurred.
     */
    public GitRepoCommandGrep(Pattern pattern, Path source, ContainerGitRepoMeta containerGitRepoMeta)
            throws IOException {
        super(pattern, source, containerGitRepoMeta);
    }

    /**
     * Start search.
     *
     * @return Search results.
     * @throws IOException IO exception.
     */
    public List<ContainerInfoSearchFileGitRepo> callGrep() throws IOException {
        try (final ObjectReader objectReader = getGitRepositoryGrep().newObjectReader()) {
            return grep(objectReader, getGitRepositoryGrep());
        }
    }

    private List<ContainerInfoSearchFileGitRepo> grep(
            final ObjectReader objectReader, final Repository repository) throws IOException {
        final List<ContainerInfoSearchFileGitRepo> infoSearchFileGitRepos = new ArrayList<>();
        ObjectId commitId = repository.resolve(Constants.HEAD);

        try (final RevWalk revWalk = new RevWalk(objectReader)) {
            try (final TreeWalk treeWalk = new TreeWalk(objectReader)) {
                RevCommit commit = revWalk.parseCommit(commitId);
                CanonicalTreeParser treeParser = new CanonicalTreeParser();
                treeParser.reset(objectReader, commit.getTree());

                int treeIndex = treeWalk.addTree(treeParser);
                treeWalk.setRecursive(true);

                while (treeWalk.next()) {
                    AbstractTreeIterator it = treeWalk.getTree(treeIndex, AbstractTreeIterator.class);
                    ObjectId objectId = it.getEntryObjectId();
                    ObjectLoader objectLoader = objectReader.open(objectId);

                    if (!isBinary(objectLoader.openStream())) {
                        List<ContainerInfoSearchFileMatcherGitRepo> matchers = getMatchedLines(
                                objectLoader.openStream(), it.getEntryPathString());
                        if (!matchers.isEmpty()) {
                            final String extensionFile = FileSystemUtils
                                    .getExtensionFromRawStrFile(it.getEntryPathString());
                            infoSearchFileGitRepos.add(new ContainerInfoSearchFileGitRepo(
                                    it.getEntryPathString(),
                                    extensionFile,
                                    GitRepo.getLinkForFile(getContainerGitRepoMeta(), it.getEntryPathString(), "master"),
                                    matchers));

                            addExtensionFilesGrep(extensionFile);
                        }

                        utilPreviewCode.clearPreviewCodes();
                        utilPreviewCode.clearUsedLineCodes();
                    }
                }
            }
        }

        return infoSearchFileGitRepos;
    }

    private List<ContainerInfoSearchFileMatcherGitRepo> getMatchedLines(
            InputStream stream,
            final String file) throws IOException {
        BufferedReader buf = null;
        try {
            List<ContainerInfoSearchFileMatcherGitRepo> matchers = new ArrayList<>();
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            buf = new BufferedReader(reader);
            String line;
            int lineNumber = 1;
            while ((line = buf.readLine()) != null) {
                Matcher matcher = getPattern().matcher(line);
                if (matcher.find()) {
                    Set<ContainerInfoSearchFileMatcherGitRepo> previewCode = new HashSet<>();
                    if (lineNumber != 1 && utilPreviewCode.getPreviewCodeLast(lineNumber) != null) {
                        final String previewCodeLineLast = utilPreviewCode.getPreviewCodeLast(lineNumber);

                        if (previewCodeLineLast == null) {
                            continue;
                        }

                        previewCode.add(new ContainerInfoSearchFileMatcherGitRepo(
                                previewCodeLineLast,
                                GitRepo.getLinkForMatcherLine(
                                        getContainerGitRepoMeta(),
                                        file,
                                        "master",
                                        utilPreviewCode.getPreviewLineNumberLast(lineNumber)),
                                utilPreviewCode.getPreviewLineNumberLast(lineNumber),
                                null
                        ));

                        if (lineNumber > 2 && previewCodeLineLast.trim().isEmpty()) {
                            final String previewCodeLineLastByStepOne = utilPreviewCode.getPreviewCodeLast(lineNumber, 1);

                            if (previewCodeLineLastByStepOne != null) {
                                previewCode.add(new ContainerInfoSearchFileMatcherGitRepo(
                                        previewCodeLineLastByStepOne,
                                        GitRepo.getLinkForMatcherLine(
                                                getContainerGitRepoMeta(),
                                                file,
                                                "master",
                                                utilPreviewCode.getPreviewLineNumberLast(lineNumber, 1)),
                                        utilPreviewCode.getPreviewLineNumberLast(lineNumber, 1),
                                        null
                                ));
                            }
                        }
                    }
                    matchers.add(new ContainerInfoSearchFileMatcherGitRepo(
                            line,
                            GitRepo.getLinkForMatcherLine(
                                    getContainerGitRepoMeta(),
                                    file,
                                    "master",
                                    lineNumber),
                            lineNumber,
                            previewCode
                    ));

                    utilPreviewCode.addUsedLineCodes(lineNumber);
                } else {
                    utilPreviewCode.addPreviewCodes(lineNumber, line);
                }

                lineNumber++;
            }
            return matchers;
        } finally {
            if (buf != null) {
                buf.close();
            }
        }
    }

}
