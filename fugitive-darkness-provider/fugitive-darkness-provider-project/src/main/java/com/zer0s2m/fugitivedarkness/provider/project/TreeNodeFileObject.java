package com.zer0s2m.fugitivedarkness.provider.project;

import com.zer0s2m.fugitivedarkness.common.support.TreeNode;

public interface TreeNodeFileObject extends TreeNode<FileProject, FileProjectJson> {

    TreeNodeFileObject addChildByLevelAndData(FileProject child, int level, FileProject data);

    TreeNodeFileObject addChildByLevel(FileProject child, int level);

    TreeNodeFileObject addChildByData(FileProject child, FileProject data);

}
