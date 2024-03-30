package com.zer0s2m.fugitivedarkness.provider.git;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Search engine for <a href="https://git-scm.com/docs/git-grep">git grep</a> command.
 */
public interface SearchEngineGrep extends SearchEngineGitUtils, SearchEngineGrepStatistics {

    /**
     * Start search.
     * <p>The search is based on the following criteria:</p>
     * <ul>
     *     <li>Including files in the search that have the specified
     *     extensions {@link SearchEngineGrep#getIncludeExtensionFilesForSearchGrep}.</li>
     *     <li>Excluding files from the search that have the specified
     *     extensions {@link SearchEngineGrep#getExcludeExtensionFilesForSearchGrep}.</li>
     *     <li>Match pattern {@link SearchEngineGrep#getPattern}.</li>
     *     <li>Include files by pattern {@link SearchEngineGrep#getPatternForIncludeFile} in the search.</li>
     *     <li>Exclude files from the search by pattern {@link SearchEngineGrep#getPatternForExcludeFile}.</li>
     *     <li>Maximum search depth {@link SearchEngineGrep#getMaxDepth()}.</li>
     *     <li>Maximum number of matches in one file {@link SearchEngineGrep#getMaxCount()}.</li>
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
     * Whether to search by file extension from filtering {@link SearchEngineGrep#getIncludeExtensionFilesForSearchGrep()}.
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
     * Set the file extension to be excluded from search filtering.
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
     * {@link SearchEngineGrep#getExcludeExtensionFilesForSearchGrep()}.
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
     * Set a pattern for files that will be included in the search.
     *
     * @param patternForIncludeFile Pattern.
     */
    void setPatternForIncludeFile(Pattern patternForIncludeFile);

    /**
     * Get a pattern for files that will be included in the search.
     *
     * @return Pattern.
     */
    Pattern getPatternForIncludeFile();

    /**
     * Should the file be searched if the pattern matches {@link SearchEngineGrep#getPatternForIncludeFile}.
     *
     * @param file The file in which the check will be carried out.
     * @return Whether to search.
     */
    default boolean getWhetherSearchByIncludeFileByPattern(final String file) {
        if (getPatternForIncludeFile() != null) {
            return getPatternForIncludeFile()
                    .matcher(FileSystemUtils.getFileName(file))
                    .find();
        }
        return true;
    }

    /**
     * Set a pattern for files that will be excluded from the search.
     *
     * @param patternForExcludeFile Pattern.
     */
    void setPatternForExcludeFile(Pattern patternForExcludeFile);

    /**
     * Get a template for files that will be excluded from the search.
     *
     * @return Pattern.
     */
    Pattern getPatternForExcludeFile();

    /**
     * Should the file be searched if the pattern matches {@link SearchEngineGrep#getPatternForExcludeFile}.
     *
     * @param file The file in which the check will be carried out.
     * @return Whether to search.
     */
    default boolean getWhetherSearchByExcludeFileByPattern(final String file) {
        if (getPatternForExcludeFile() != null) {
            return getPatternForExcludeFile()
                    .matcher(FileSystemUtils.getFileName(file))
                    .find();
        }
        return false;
    }

    /**
     * Set a limit on the number of matches per file.
     * <a href="https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---max-countltnumgt">More about</a>.
     *
     * @param maxCount Limit.
     * @throws SearchEngineGitSetMaxCountException Exception for setting the maximum number of matches in one file.
     */
    void setMaxCount(int maxCount) throws SearchEngineGitSetMaxCountException;

    /**
     * Get the limit on the number of matches per file.
     *
     * @return limit
     */
    int getMaxCount();

    /**
     * Set the maximum search depth.
     * <a href="https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---max-depthltdepthgt">More about</a>.
     *
     * @param maxDepth Depth.
     * @throws SearchEngineGitSetMaxDepthException Exception for setting maximum search depth.
     */
    void setMaxDepth(int maxDepth) throws SearchEngineGitSetMaxDepthException;

    /**
     * Get maximum search depth.
     *
     * @return Depth.
     */
    int getMaxDepth();

    /**
     * Whether the original path matches the maximum depth.
     *
     * @param source Source file.
     * @return Is it compliant.
     */
    default boolean whetherSourceMatchesMaximumDepth(String source) {
        if (getMaxDepth() == -1) {
            return true;
        }
        final String[] sourceSplit = source.split("/");
        return sourceSplit.length == getMaxDepth() || sourceSplit.length < getMaxDepth();
    }

    /**
     * Set the code preview <b>after</b> and <b>before</b> the match.
     *
     * @param context Code preview.
     * @throws SearchEngineGitSetContextException Exception for setting preview code after and before matches.
     */
    default void setContext(int context) throws SearchEngineGitSetContextException {
        setContextBefore(context);
        setContextAfter(context);
    }

    /**
     * Set code preview <b>before</b> showing match.
     * <a href="https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---before-contextltnumgt">More about</a>.
     *
     * @param contextBefore Preview.
     * @throws SearchEngineGitSetContextException Exception for setting preview code after and before matches.
     */
    void setContextBefore(int contextBefore) throws SearchEngineGitSetContextException;

    /**
     * Get a code preview <b>before</b> showing a match.
     *
     * @return Preview.
     */
    int getContextBefore();

    /**
     * Set code preview <b>after</b> showing a match.
     * <a href="https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---after-contextltnumgt">More about</a>.
     *
     * @param contextAfter Preview.
     * @throws SearchEngineGitSetContextException Exception for setting preview code after and before matches.
     */
    void setContextAfter(int contextAfter) throws SearchEngineGitSetContextException;

    /**
     * Get a code preview <b>after</b> showing a match.
     *
     * @return Preview.
     */
    int getContextAfter();

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
