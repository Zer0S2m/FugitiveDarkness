package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.ContainerGitRepoMeta;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

interface SearchFilterCallable<T> extends Callable<T> {

    void setPattern(Pattern pattern);

    void setMaxCount(int maxCount);

    void setFile(String file);

    void setCurrentBranch(String currentBranch);

    void setContainerGitRepoMeta(ContainerGitRepoMeta containerGitRepoMeta);

    void setIsUseMatcherCounterInFile(boolean isUseMatcherCounterInFile);

    void setContextBeforeReal(int contextBeforeReal);

    void setContextAfterReal(int contextAfterReal);

}
