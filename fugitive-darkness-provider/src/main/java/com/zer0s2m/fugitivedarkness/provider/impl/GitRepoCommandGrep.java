package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.provider.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.RawText;
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
 * Service for providing search in git repositories. Similar to the grep command in git.
 */
class GitRepoCommandGrep {

    private final Pattern pattern;

    private final Path source;

    private final Set<String> extensionFiles = new HashSet<>();

    private final ContainerGitRepoMeta containerGitRepoMeta;

    public GitRepoCommandGrep(Pattern pattern, Path source, ContainerGitRepoMeta containerGitRepoMeta) {
        this.pattern = pattern;
        this.source = source;
        this.containerGitRepoMeta = containerGitRepoMeta;
    }

    public List<ContainerInfoSearchFileGitRepo> call() throws IOException {
        final Repository repository = Git.open(source.toFile())
                .checkout()
                .getRepository();

        try (final ObjectReader objectReader = repository.newObjectReader()) {
            return grep(objectReader, repository);
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
                                    GitRepo.getLinkForFile(containerGitRepoMeta, it.getEntryPathString(), "master"),
                                    matchers));

                            extensionFiles.add(extensionFile);
                        }
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
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    matchers.add(new ContainerInfoSearchFileMatcherGitRepo(
                            line,
                            GitRepo.getLinkForMatcherLine(
                                    containerGitRepoMeta,
                                    file,
                                    "master",
                                    lineNumber),
                            lineNumber));
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

    private static boolean isBinary(InputStream stream) throws IOException {
        try (stream) {
            return RawText.isBinary(stream);
        }
    }

    public Set<String> getExtensionFiles() {
        return extensionFiles;
    }

}
