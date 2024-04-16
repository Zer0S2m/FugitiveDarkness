package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ProjectManagerImpl implements ProjectManager {

    private ProjectReaderAdapterAbstract adapterReader;

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

    @Override
    public TreeNodeFileObject collectTreeFilesProject(Collection<FileProject> filesProject) {
        ProjectManagerTreeNodeFileObject nodeFileObject = new ProjectManagerTreeNodeFileObject();
        return nodeFileObject.iter(filesProject);
    }

    @Override
    public void setAdapterReader(ProjectReaderAdapterAbstract adapterReader) {
        this.adapterReader = adapterReader;
    }

    @Override
    public ProjectReaderAdapterAbstract getAdapterReader() {
        return adapterReader;
    }

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
