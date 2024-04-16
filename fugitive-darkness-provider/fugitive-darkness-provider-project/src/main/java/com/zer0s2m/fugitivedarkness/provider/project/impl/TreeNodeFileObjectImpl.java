package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.common.support.TreeNode;
import com.zer0s2m.fugitivedarkness.common.support.TreeNodeImpl;
import com.zer0s2m.fugitivedarkness.provider.project.FileProject;
import com.zer0s2m.fugitivedarkness.provider.project.FileProjectJson;
import com.zer0s2m.fugitivedarkness.provider.project.TreeNodeFileObject;

import java.util.ArrayList;
import java.util.Collection;

public class TreeNodeFileObjectImpl extends TreeNodeImpl<FileProject, FileProjectJson>
        implements TreeNodeFileObject {

    public TreeNodeFileObjectImpl(FileProject data) {
        super(data);
    }

    @Override
    public TreeNodeFileObject addChildByLevelAndData(FileProject child, int level, FileProject data) {
        for (TreeNode<FileProject, FileProjectJson> element : elementsIndex) {
            if (element.getLevel() == level && element.getData().equals(data)) {
                TreeNodeFileObjectImpl childNode = new TreeNodeFileObjectImpl(child);
                childNode.setParent(this);
                element.addChild(childNode);
                registerChildForSearch(childNode);
                return childNode;
            }
        }

        return null;
    }

    @Override
    public TreeNodeFileObject addChildByLevel(FileProject child, int level) {
        for (TreeNode<FileProject, FileProjectJson> element : elementsIndex) {
            if (element.getLevel() == level) {
                TreeNodeFileObjectImpl childNode = new TreeNodeFileObjectImpl(child);
                childNode.setParent(this);
                element.addChild(childNode);
                registerChildForSearch(childNode);
                return childNode;
            }
        }

        return null;
    }

    @Override
    public TreeNodeFileObject addChildByData(FileProject child, FileProject data) {
        for (TreeNode<FileProject, FileProjectJson> element : elementsIndex) {
            if (element.getData().equals(data)) {
                TreeNodeFileObjectImpl childNode = new TreeNodeFileObjectImpl(child);
                childNode.setParent(this);
                element.addChild(childNode);
                registerChildForSearch(childNode);
                return childNode;
            }
        }

        return null;
    }

    @Override
    public FileProjectJson toDTO() {
        Collection<FileProjectJson> fileProjectJsons = new ArrayList<>();

        getChildren().forEach(child -> fileProjectJsons.add(child.toDTO()));

        String[] pathSplit = getData().path().split("/");

        return new FileProjectJson(
                getData().path(),
                pathSplit[pathSplit.length - 1],
                getData().isFile(),
                getData().isDirectory(),
                fileProjectJsons
        );
    }

}
