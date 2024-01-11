package com.zer0s2m.fugitivedarkness.provider;

import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Search engine for <a href="https://git-scm.com/docs/git-grep">git grep</a> command.
 */
public interface SearchEngineGitGrep extends SearchEngineGitUtils {

    /**
     * Start search.
     *
     * @return Search results.
     * @throws IOException IO exception.
     */
    List<ContainerInfoSearchFileGitRepo> callGrep() throws IOException;

    /**
     * Get file extensions that participated in the search.
     *
     * @return File extension.
     */
    Set<String> getExtensionFilesGrep();

    /**
     * Add extension file.
     *
     * @param fileExtension File extension.
     * @return Whether an object has been added.
     */
    boolean addExtensionFilesGrep(String fileExtension);

    /**
     * Install the git repository via its source path.
     *
     * @param source Source path of the repository.
     * @throws IOException If an IO error occurred.
     */
    void setGitRepositoryGrep(Path source) throws IOException;

    /**
     * Get the git repository.
     *
     * @return Git repository.
     */
    Repository getGitRepositoryGrep();

    /**
     * Set a pattern to search for matches.
     *
     * @param pattern Match pattern.
     */
    void setPattern(Pattern pattern);

    /**
     * Get a pattern to search for matches.
     *
     * @return A pattern for finding matches in files.
     */
    Pattern getPattern();

    /**
     * Set additional Information.
     *
     * @param containerGitRepoMeta Additional Information.
     */
    void setContainerGitRepoMeta(ContainerGitRepoMeta containerGitRepoMeta);

    /**
     * Get more information.
     *
     * @return Additional Information.
     */
    ContainerGitRepoMeta getContainerGitRepoMeta();

}
