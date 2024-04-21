package com.zer0s2m.fugitivedarkness.common.support;

import java.util.Collection;

public interface TreeNode<T, D> {

    TreeNode<T, D> addChild(T child);

    TreeNode<T, D> findTreeNode(T cmp);

    boolean isRoot();

    int getLevel();

    T getData();

    TreeNode<T, D> getParent();

    void setParent(TreeNode<T, D> parent);

    Collection<TreeNode<T, D>> getChildren();

    void addChild(TreeNode<T, D> child);

    void registerChildForSearch(TreeNode<T, D> node);

    default D toDTO() {
        return null;
    }

}
