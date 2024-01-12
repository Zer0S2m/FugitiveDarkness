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
     * <p>The search is based on the following criteria:</p>
     * <ul>
     *     <li>Including files in the search that have the specified
     *     extensions {@link SearchEngineGitGrep#getIncludeExtensionFilesForSearchGrep}.</li>
     *     <li>Excluding files from the search that have the specified
     *     extensions {@link SearchEngineGitGrep#getExcludeExtensionFilesForSearchGrep}.</li>
     *     <li>Match pattern {@link SearchEngineGitGrep#getPattern}.</li>
     * </ul>
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
     * Set the file extension that will be included in the search filtering.
     *
     * @param extensionFiles File extension.
     */
    void setIncludeExtensionFilesForSearchGrep(Set<String> extensionFiles);

    /**
     * Get file extensions that will be included in file filtering when searching for matches.
     *
     * @return File extension.
     */
    Set<String> getIncludeExtensionFilesForSearchGrep();

    /**
     * Whether to search by file extension from filtering {@link SearchEngineGitGrep#getIncludeExtensionFilesForSearchGrep()}.
     *
     * @param extensionFile File extension.
     * @return Whether to search.
     */
    default boolean getWhetherSearchByIncludeFileExtension(final String extensionFile) {
        if (getIncludeExtensionFilesForSearchGrep() != null && !getIncludeExtensionFilesForSearchGrep().isEmpty()) {
            return getIncludeExtensionFilesForSearchGrep().contains(extensionFile);
        }
        return true;
    }

    /**
     * Set the file extension to be excluded from search filtering..
     *
     * @param extensionFiles File extension.
     */
    void setExcludeExtensionFilesForSearchGrep(Set<String> extensionFiles);

    /**
     * Get file extensions that will be excluded from file filtering when searching for matches.
     *
     * @return File extension.
     */
    Set<String> getExcludeExtensionFilesForSearchGrep();

    /**
     * Should search by file extension be excluded from filtering
     * {@link SearchEngineGitGrep#getExcludeExtensionFilesForSearchGrep()}.
     *
     * @param extensionFile File extension.
     * @return Exclude from search.
     */
    default boolean getWhetherSearchByExcludeFileExtension(final String extensionFile) {
        if (getExcludeExtensionFilesForSearchGrep() != null && !getExcludeExtensionFilesForSearchGrep().isEmpty()) {
            return getExcludeExtensionFilesForSearchGrep().contains(extensionFile);
        }
        return false;
    }

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
