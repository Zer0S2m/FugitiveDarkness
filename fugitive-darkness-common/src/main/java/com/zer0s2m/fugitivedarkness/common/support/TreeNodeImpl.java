package com.zer0s2m.fugitivedarkness.common.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreeNodeImpl<T, D> implements TreeNode<T, D> {

    private final T data;

    private TreeNode<T, D> parent;

    private final List<TreeNode<T, D>> children = new ArrayList<>();

    protected final List<TreeNode<T, D>> elementsIndex = new ArrayList<>();

    public TreeNodeImpl(T data) {
        this.data = data;
        elementsIndex.add(this);
    }

    @Override
    public TreeNode<T, D> addChild(T child) {
        TreeNode<T, D> childNode = new TreeNodeImpl<>(child);
        childNode.setParent(this);
        addChild(childNode);
        registerChildForSearch(childNode);
        return childNode;
    }

    @Override
    public void registerChildForSearch(TreeNode<T, D> node) {
        elementsIndex.add(node);
        if (parent != null) {
            parent.registerChildForSearch(node);
        }
    }

    @Override
    public TreeNode<T, D> findTreeNode(T cmp) {
        for (TreeNode<T, D> element : elementsIndex) {
            if (cmp.equals(element.getData())) {
                return element;
            }
        }

        return null;
    }

    @Override
    public boolean isRoot() {
        return parent == null;
    }

    @Override
    public int getLevel() {
        if (this.isRoot()) {
            return 0;
        }
        else {
            return parent.getLevel() + 1;
        }
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public TreeNode<T, D> getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode<T, D> parent) {
        this.parent = parent;
    }

    @Override
    public Collection<TreeNode<T, D>> getChildren() {
        return children;
    }

    @Override
    public void addChild(TreeNode<T, D> child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "[data null]";
    }

}
