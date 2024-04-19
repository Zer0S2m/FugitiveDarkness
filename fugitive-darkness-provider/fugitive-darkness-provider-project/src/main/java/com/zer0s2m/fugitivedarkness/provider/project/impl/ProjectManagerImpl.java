package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoSearchFileGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoSearchGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoFilterSearch;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoManager;
import com.zer0s2m.fugitivedarkness.provider.project.*;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

/**
 * A class for project support in the form of:
 * <ul>
 *     <li>Git repositories.</li>
 *     <li>Local projects</li>
 * </ul>
 * Getting project statistics:
 * <ul>
 *     <li>Information about commits in a specific file.</li>
 *     <li>Design hotspots.</li>
 *     <li>The number of lines of code in certain file objects.</li>
 *     <li>The number of todos.</li>
 * </ul>
 */
public class ProjectManagerImpl implements ProjectManager {

    private ProjectReaderAdapterAbstract projectReaderAdapterAbstract;

    private ProjectCountLineFilesReaderAdapterAbstract projectCountFilesReaderAdapterAbstract;

    static final String REGEX_FIND_TODO = "(T|d)(O|o)(D|d)(O|o)";

    /**
     * Get all the information about the file objects in the project.
     *
     * @param reader A reader for a specific type of project.
     * @return Information about the file objects in the project.
     */
    @Override
    public Collection<FileProject> getAllFilesProject(ProjectReader reader) {
        return reader.read(getAdapterReader());
    }

    /**
     * Get system design hotspots - the largest number of commits in files.
     *
     * @param sourceGitRepository The source path to the repository.
     * @param files               Relative file paths.
     * @return Information about the number of commits in the files.
     */
    @Override
    public Collection<FileHotspotProject> designHotspots(Path sourceGitRepository, Collection<String> files) {
        Collection<FileHotspotProject> fileHotspotProjects = new ArrayList<>();
        GitRepoManager gitRepoManager = GitRepoManager.create();

        Map<String, Iterable<RevCommit>> commitsFiles = gitRepoManager.gGetAllCommitsOfFiles(
                sourceGitRepository, files);

        commitsFiles.forEach((file, commitsIterable) -> {
            long countCommits = StreamSupport.stream(commitsIterable.spliterator(), false).count();
            fileHotspotProjects.add(new FileHotspotProject(
                    file,
                    countCommits
            ));
        });

        return fileHotspotProjects;
    }

    /**
     * Get information from the last commit in a specific file.
     *
     * @param sourceGitRepository The source path to the repository.
     * @param file                The path to the file where the last commit will be searched.
     * @return Information from the last commit.
     */
    @Override
    public FileCommitInfo lastCommitOfFile(Path sourceGitRepository, String file) {
        GitRepoManager gitRepoManager = GitRepoManager.create();

        return infoCommitOfFile(gitRepoManager.gLastCommitOfFile(
                sourceGitRepository, file));
    }

    /**
     * Get information from the first commit in a specific file.
     *
     * @param sourceGitRepository The source path to the repository.
     * @param file                The path to the file where the first commit will be searched.
     * @return Information from the first commit.
     */
    @Override
    public FileCommitInfo firstCommitOfFile(Path sourceGitRepository, String file) {
        GitRepoManager gitRepoManager = GitRepoManager.create();

        return infoCommitOfFile(gitRepoManager.gFirstCommitOfFile(
                sourceGitRepository, file));
    }

    /**
     * Get information from the all commits in a specific file.
     *
     * @param sourceGitRepository The source path to the repository.
     * @param file                The path to the file where the all commits will be searched.
     * @return Information from the all commits.
     */
    @Override
    public Collection<FileCommitInfo> allCommitOfFile(Path sourceGitRepository, String file) {
        GitRepoManager gitRepoManager = GitRepoManager.create();

        Collection<RevCommit> revCommits = gitRepoManager.gAllCommitIfFile(
                sourceGitRepository, file);
        Collection<FileCommitInfo> fileCommitInfos = new ArrayList<>();

        revCommits.forEach(revCommit -> fileCommitInfos.add(infoCommitOfFile(revCommit)));

        return fileCommitInfos;
    }

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
    @Override
    public Collection<FileProjectCountLine> countLinesCodeFile(
            ProjectCountLineFilesFilters filters, ProjectCountLineFilesReader reader)
            throws ProjectException, IOException {
        return reader.read(getAdapterCountFiles(), filters.getTypeFileObject());
    }

    /**
     * Get all matches of the todos pattern in the git repository or in a local project.
     *
     * @param sourceGitRepository The original path to the project.
     * @param filters             Additional filters for launching the search engine.
     * @return The search result.
     */
    @Override
    public List<ContainerInfoSearchFileGitRepo> findTodo(Path sourceGitRepository, ProjectTodoFilters filters) {
        GitRepoManager gitRepoManager = GitRepoManager.create();

        GitRepoFilterSearch gitRepoFilterSearch = GitRepoFilterSearch
                .create()
                .setPattern(REGEX_FIND_TODO)
                .setIsSearchOnlyInArea(true)
                .addGitRepo(sourceGitRepository)
                .setAreaIsDirectory(filters.getIsDirectory())
                .setAreaIsFile(filters.getIsFile())
                .setAreaFile(filters.getFile())
                .addGitMeta(sourceGitRepository, filters.getContainerGitRepoMeta());

        final List<ContainerInfoSearchGitRepo> resultSearch;

        if (filters.getIsLocalGitRepo()) {
            resultSearch = gitRepoManager.searchByGrep_io(gitRepoFilterSearch);
        } else if (filters.getIsUnpackingGitRepo()) {
            resultSearch = gitRepoManager.searchByGrep_io(gitRepoFilterSearch);
        } else {
            resultSearch = gitRepoManager.searchByGrep_jgit(gitRepoFilterSearch);
        }

        return resultSearch.get(0).found();
    }

    /**
     * Collect information from the commit.
     *
     * @param commit The commit.
     * @return Information from the commit.
     */
    private FileCommitInfo infoCommitOfFile(RevCommit commit) {
        String justTheAuthorNoTime = commit
                .getAuthorIdent()
                .toExternalString()
                .split(">")[0] + ">";
        String[] justTheAuthorNoTimeSplit = justTheAuthorNoTime.split(" ");
        ZoneId zoneId = commit.getAuthorIdent().getTimeZone().toZoneId();
        Instant commitInstant = Instant.ofEpochSecond(commit.getCommitTime());
        ZonedDateTime authorDateTime = ZonedDateTime.ofInstant(commitInstant, zoneId);
        String gitDateTimeFormatString = "EEE MMM dd HH:mm:ss yyyy Z";
        String formattedDate = authorDateTime.format(DateTimeFormatter.ofPattern(gitDateTimeFormatString));
        String tabbedCommitMessage =
                String.join("", commit
                        .getFullMessage()
                        .split("\\r?\\n"));

        return new FileCommitInfo(
                commit.getName(),
                justTheAuthorNoTimeSplit[0],
                justTheAuthorNoTimeSplit[1]
                        .replace("<", "")
                        .replace(">", ""),
                formattedDate,
                tabbedCommitMessage);
    }

    /**
     * Assemble the file structure as a tree structure.
     *
     * @param filesProject The project files are in the form of a collection.
     * @return The tree structure of the project files.
     */
    @Override
    public TreeNodeFileObject collectTreeFilesProject(Collection<FileProject> filesProject) {
        ProjectManagerTreeNodeFileObject nodeFileObject = new ProjectManagerTreeNodeFileObject();
        return nodeFileObject.iter(filesProject);
    }

    /**
     * Install the adapter for the git repository reader.
     *
     * @param adapterReader Adapter for the git repository reader.
     */
    @Override
    public void setAdapterReader(ProjectReaderAdapterAbstract adapterReader) {
        this.projectReaderAdapterAbstract = adapterReader;
    }

    /**
     * Get an adapter for the git repository reader.
     *
     * @return Adapter for the git repository reader.
     */
    @Override
    public ProjectReaderAdapterAbstract getAdapterReader() {
        return projectReaderAdapterAbstract;
    }

    /**
     * Install an adapter to collect statistics from git repositories.
     *
     * @param adapterCountFiles An adapter for collecting statistics from git repositories.
     */
    @Override
    public void setAdapterCountFiles(ProjectCountLineFilesReaderAdapterAbstract adapterCountFiles) {
        this.projectCountFilesReaderAdapterAbstract = adapterCountFiles;
    }

    /**
     * Install an adapter to collect statistics from local projects.
     *
     * @return An adapter for collecting statistics from git repositories.
     */
    @Override
    public ProjectCountLineFilesReaderAdapterAbstract getAdapterCountFiles() {
        return projectCountFilesReaderAdapterAbstract;
    }

    /**
     * An assistant for converting relative file paths into a project tree structure.
     */
    private static class ProjectManagerTreeNodeFileObject {

        private final AtomicInteger levelTree = new AtomicInteger(0);

        private final TreeNodeFileObject rootNode = new TreeNodeFileObjectImpl(new FileProject(
                "ROOT",
                false,
                false));

        private final Map<String, TreeNodeFileObject> lastDirectories = new HashMap<>();

        public TreeNodeFileObject iter(Collection<FileProject> filesProject) {
            Collection<FileProject> rawProjectFiles = new ArrayList<>();

            filesProject.forEach(fileProject -> {
                List<String> splitPath = splitPath(fileProject.path());

                if (splitPath.size() - 1 == levelTree.get()) { // Level node
                    TreeNodeFileObject node = null;
                    if (levelTree.get() == 0) {
                        node = rootNode.addChildByLevel(fileProject, levelTree.get());
                    } else {
                        List<String> pathSplitFile = splitPath(fileProject.path());
                        pathSplitFile.removeLast(); // Delete file name
                        String cleanPathFileProject = String.join("/", pathSplitFile);

                        if (lastDirectories.containsKey(cleanPathFileProject)) {
                            node = rootNode.addChildByData(
                                    fileProject, lastDirectories.get(cleanPathFileProject).getData());
                        }
                    }

                    if (fileProject.isDirectory() && node != null) {
                        lastDirectories.put(fileProject.path(), node);
                    }
                } else {
                    rawProjectFiles.add(fileProject);
                }
            });

            if (!rawProjectFiles.isEmpty()) {
                levelTree.set(levelTree.get() + 1);
                iter(rawProjectFiles);
            }

            return rootNode;
        }

        private List<String> splitPath(final String path) {
            return new ArrayList<>(Arrays.asList(path.split("/")));
        }

    }

}
