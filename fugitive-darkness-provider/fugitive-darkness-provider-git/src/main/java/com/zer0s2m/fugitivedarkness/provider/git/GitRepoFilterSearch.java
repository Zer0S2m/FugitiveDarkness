package com.zer0s2m.fugitivedarkness.provider.git;

import com.zer0s2m.fugitivedarkness.provider.git.impl.GitRepoFilterSearchImpl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Filter by searching git repository. Similar to grep command in git.
 */
public interface GitRepoFilterSearch {

    /**
     * Set the path to the git repository for later searching.
     *
     * @param source Path to git repository.
     * @return Search filter.
     */
    GitRepoFilterSearch addGitRepo(Path source);

    /**
     * Set the path to the git repository for later searching.
     *
     * @param source A collection of paths to git repositories.
     * @return Search filter.
     */
    GitRepoFilterSearch addGitRepo(Collection<Path> source);

    /**
     * Add metadata for the repository.
     *
     * @param source Source path of the git repository.
     * @param meta   Git repository information.
     * @return Search filter.
     */
    GitRepoFilterSearch addGitMeta(Path source, ContainerGitRepoMeta meta);

    /**
     * Get metadata for a git repository.
     *
     * @param source Source path of the git repository.
     * @return Git repository information.
     */
    ContainerGitRepoMeta getGitMeta(Path source);

    /**
     * Set a pattern to search for matches.
     *
     * @param pattern Match pattern.
     * @return Search filter.
     */
    GitRepoFilterSearch setPattern(String pattern);

    /**
     * Set a pattern to search for matches.
     *
     * @param pattern Match pattern.
     * @return Search filter.
     */
    GitRepoFilterSearch setPattern(Pattern pattern);

    /**
     * Get pattern for matches.
     *
     * @return Match pattern.
     */
    Pattern getPattern();

    /**
     * Get a collection of paths to git repositories.
     *
     * @return A collection of paths to git repositories.
     */
    Set<Path> getSources();

    /**
     * Set the file extension that will be included in the search filtering.
     *
     * @param extensionFiles File extension.
     * @return Search filter.
     */
    GitRepoFilterSearch setIncludeExtensionFile(Collection<String> extensionFiles);

    /**
     * Set the file extension that will be included in the search filtering.
     *
     * @param extensionFile File extension.
     * @return Search filter.
     */
    GitRepoFilterSearch setIncludeExtensionFile(String extensionFile);

    /**
     * Set file extensions that will be included in file filtering when searching for matches.
     *
     * @return File extension.
     */
    Set<String> getIncludeExtensionFile();

    /**
     * Set the file extension to be excluded from search filtering.
     *
     * @param extensionFiles File extension.
     * @return Search filter.
     */
    GitRepoFilterSearch setExcludeExtensionFile(Collection<String> extensionFiles);

    /**
     * Set the file extension to be excluded from search filtering.
     *
     * @param extensionFile File extension.
     * @return Search filter.
     */
    GitRepoFilterSearch setExcludeExtensionFile(String extensionFile);

    /**
     * Get file extensions that will be excluded from file filtering when searching for matches.
     *
     * @return File extension.
     */
    Set<String> getExcludeExtensionFile();

    /**
     * Set a pattern for files that will be included in the search.
     *
     * @param patternForIncludeFile Pattern.
     * @return Search filter.
     */
    GitRepoFilterSearch setPatternForIncludeFile(Pattern patternForIncludeFile);

    /**
     * Get a pattern for files that will be included in the search.
     *
     * @return Pattern.
     */
    Pattern getPatternForIncludeFile();

    /**
     * Set a pattern for files that will be excluded from the search.
     *
     * @param patternForExcludeFile Pattern.
     * @return Search filter.
     */
    GitRepoFilterSearch setPatternForExcludeFile(Pattern patternForExcludeFile);

    /**
     * Get a template for files that will be excluded from the search.
     *
     * @return Pattern.
     */
    Pattern getPatternForExcludeFile();

    /**
     * Set a limit on the number of matches per file.
     * <a href="https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---max-countltnumgt">More about</a>.
     *
     * @param maxCount Limit.
     * @return Search filter.
     */
    GitRepoFilterSearch setMaxCount(int maxCount);

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
     * @return Search filter.
     */
    GitRepoFilterSearch setMaxDepth(int maxDepth);

    /**
     * Get maximum search depth.
     *
     * @return Depth.
     */
    int getMaxDepth();

    /**
     * Set the code preview <b>after</b> and <b>before</b> the match.
     *
     * @param context Code preview.
     * @return Search filter.
     */
    GitRepoFilterSearch setContext(int context);

    /**
     * Get the code preview after and before the match.
     *
     * @return Code preview.
     */
    int getContext();

    /**
     * Set code preview <b>before</b> showing match.
     * <a href="https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---before-contextltnumgt">More about</a>.
     *
     * @param contextBefore Preview.
     */
    GitRepoFilterSearch setContextBefore(int contextBefore);

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
     */
    GitRepoFilterSearch setContextAfter(int contextAfter);

    /**
     * Get a code preview <b>after</b> showing a match.
     *
     * @return Preview.
     */
    int getContextAfter();

    /**
     * Set a sign that what should be searched for only in a certain area
     *
     * @param isSearchOnlyInArea Search only in a certain area.
     */
    GitRepoFilterSearch setIsSearchOnlyInArea(boolean isSearchOnlyInArea);

    /**
     * Get a sign that you need to look for something only in a certain area
     *
     * @return Search only in a certain area.
     */
    boolean getIsSearchOnlyInArea();

    /**
     * Set the search area to a file or directory.
     *
     * @param areaFile The search area is a file or directory.
     */
    GitRepoFilterSearch setAreaFile(String areaFile);

    /**
     * Get the search area to a file or directory.
     *
     * @return The search area is a file or directory.
     */
    String getAreaFile();

    /**
     * Set the indication that the search area is a file.
     *
     * @param areaIsFile Indicates that the search area is a file.
     */
    GitRepoFilterSearch setAreaIsFile(boolean areaIsFile);

    /**
     * Get the indication that the search area is a file.
     *
     * @return Indicates that the search area is a file.
     */
    boolean getAreaIsFile();

    /**
     * Get the indication that the search area is a file.
     *
     * @param areaIsDirectory Indicates that the search area is a directory.
     */
    GitRepoFilterSearch setAreaIsDirectory(boolean areaIsDirectory);

    /**
     * Get the indication that the search area is a file.
     *
     * @return Indicates that the search area is a directory.
     */
    boolean getAreaIsDirectory();

    /**
     * Clear information about repositories.
     * <p>Relevant when calling the cloning method {@link GitRepoFilterSearch#clone(GitRepoFilterSearch)}.</p>
     */
    void clearGitMeta();

    /**
     * Clear repository source paths.
     * <p>Relevant when calling the cloning method {@link GitRepoFilterSearch#clone(GitRepoFilterSearch)}.</p>
     */
    void clearSources();

    static GitRepoFilterSearch create() {
        return new GitRepoFilterSearchImpl();
    }

    static GitRepoFilterSearch clone(final GitRepoFilterSearch real) {
        final GitRepoFilterSearch newFilter = new GitRepoFilterSearchImpl()
                .setContext(real.getContext())
                .addGitRepo(real.getSources())
                .setContextBefore(real.getContextBefore())
                .setContextAfter(real.getContextAfter())
                .setMaxDepth(real.getMaxDepth())
                .setMaxCount(real.getMaxCount())
                .setIncludeExtensionFile(real.getIncludeExtensionFile())
                .setExcludeExtensionFile(real.getExcludeExtensionFile())
                .setPatternForIncludeFile(real.getPatternForIncludeFile())
                .setPatternForExcludeFile(real.getPatternForExcludeFile())
                .setPattern(real.getPattern())
                .setIsSearchOnlyInArea(real.getIsSearchOnlyInArea())
                .setAreaFile(real.getAreaFile())
                .setAreaIsFile(real.getAreaIsFile())
                .setAreaIsDirectory(real.getAreaIsDirectory());

        real.getSources().forEach((source) -> newFilter.addGitMeta(source, real.getGitMeta(source)));

        return newFilter;
    }

}
