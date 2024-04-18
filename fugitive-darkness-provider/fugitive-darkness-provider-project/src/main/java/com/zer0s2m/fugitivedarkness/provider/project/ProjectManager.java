package com.zer0s2m.fugitivedarkness.provider.project;

import com.zer0s2m.fugitivedarkness.provider.project.impl.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

/**
 * An interface for implementing a class for project maintenance in the form of:
 * <ul>
 *     <li>Git repositories.</li>
 *     <li>Local projects</li>
 * </ul>
 * Getting project statistics:
 * <ul>
 *     <li>Information about commits in a specific file.</li>
 *     <li>Design hotspots.</li>
 *     <li>The number of lines of code in certain file objects.</li>
 * </ul>
 */
public interface ProjectManager {

    /**
     * Get all the information about the file objects in the project.
     *
     * @param reader A reader for a specific type of project.
     * @return Information about the file objects in the project.
     */
    Collection<FileProject> getAllFilesProject(ProjectReader reader);

    /**
     * Get system design hotspots - the largest number of commits in files.
     *
     * @param sourceGitRepository The source path to the repository.
     * @param files               Relative file paths.
     * @return Information about the number of commits in the files.
     */
    Collection<FileHotspotProject> designHotspots(Path sourceGitRepository, Collection<String> files);

    /**
     * Get information from the last commit in a specific file.
     *
     * @param sourceGitRepository The source path to the repository.
     * @param file                The path to the file where the last commit will be searched.
     * @return Information from the last commit.
     */
    FileCommitInfo lastCommitOfFile(Path sourceGitRepository, String file);

    /**
     * Get information from the first commit in a specific file.
     *
     * @param sourceGitRepository The source path to the repository.
     * @param file                The path to the file where the first commit will be searched.
     * @return Information from the first commit.
     */
    FileCommitInfo firstCommitOfFile(Path sourceGitRepository, String file);

    /**
     * Get information from the all commits in a specific file.
     *
     * @param sourceGitRepository The source path to the repository.
     * @param file                The path to the file where the all commits will be searched.
     * @return Information from the all commits.
     */
    Collection<FileCommitInfo> allCommitOfFile(Path sourceGitRepository, String file);

    /**
     * Get information about the number of rows in a file object.
     *
     * @param filters A filter to run a specific row counting script.
     *                <ul>
     *                <li>Counting rows in a directory.</li>
     *                <li>Counting the lines in the file.</li>
     *                </ul>
     * @param reader  A reader for counting the number of lines in a file object.
     * @return Information about the number of lines in the file object.
     */
    Collection<FileProjectCountLine> countLinesCodeFile(
            ProjectCountLineFilesFilters filters, ProjectCountLineFilesReader reader) throws ProjectException, IOException;

    /**
     * Assemble the file structure as a tree structure.
     *
     * @param filesProject The project files are in the form of a collection.
     * @return The tree structure of the project files.
     */
    TreeNodeFileObject collectTreeFilesProject(Collection<FileProject> filesProject);

    /**
     * Install the adapter for the git repository reader.
     *
     * @param adapterReader Adapter for the git repository reader.
     */
    void setAdapterReader(ProjectReaderAdapterAbstract adapterReader);

    /**
     * Get an adapter for the git repository reader.
     *
     * @return Adapter for the git repository reader.
     */
    ProjectReaderAdapterAbstract getAdapterReader();

    /**
     * Install an adapter to collect statistics from git repositories.
     *
     * @param adapterCountFiles An adapter for collecting statistics from git repositories.
     */
    void setAdapterCountFiles(ProjectCountLineFilesReaderAdapterAbstract adapterCountFiles);

    /**
     * Install an adapter to collect statistics from local projects.
     *
     * @return An adapter for collecting statistics from git repositories.
     */
    ProjectCountLineFilesReaderAdapterAbstract getAdapterCountFiles();

    static ProjectManager create() {
        return new ProjectManagerImpl();
    }

    /**
     * Create a manager to maintain projects from git repositories.
     *
     * @return Project manager.
     */
    static ProjectManager createGit() {
        ProjectManager projectManager = create();
        projectManager.setAdapterReader(new ProjectReaderAdapterGit());
        projectManager.setAdapterCountFiles(new ProjectCountLineFilesReaderAdapterGit());
        return projectManager;
    }

    /**
     * Create a manager to serve local projects.
     *
     * @return Project manager.
     */
    static ProjectManager createLocal() {
        ProjectManager projectManager = create();
        projectManager.setAdapterReader(new ProjectReaderAdapterLocal());
        projectManager.setAdapterCountFiles(new ProjectCountLineFilesReaderAdapterLocal());
        return projectManager;
    }

}
