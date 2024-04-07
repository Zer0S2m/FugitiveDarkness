package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoSearchFileGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoSearchFileMatcherGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.FileSystemUtils;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoUtils;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;

class SearchIOFileCallable extends SearchInFileMatchFilterCallableAbstract<ContainerInfoSearchFileGitRepo> {

    /**
     * The original path to the file.
     */
    private final Path file;

    /**
     * @param file The original path to the file.
     */
    public SearchIOFileCallable(Path file) {
        this.file = file;
    }

    @Override
    public ContainerInfoSearchFileGitRepo call() throws Exception {
        try (final Reader reader = new FileReader(file.toFile())) {
            final SearchInFileMatchFilterCallableAbstract<List<ContainerInfoSearchFileMatcherGitRepo>> searchInFileMatchCallable =
                    new SearchInFileMatchCallable(reader);

            searchInFileMatchCallable.setContainerGitRepoMeta(containerGitRepoMeta);
            searchInFileMatchCallable.setFile(file.toString());
            searchInFileMatchCallable.setCurrentBranch(currentBranch);
            searchInFileMatchCallable.setIsUseMatcherCounterInFile(isUseMatcherCounterInFile);
            searchInFileMatchCallable.setMaxCount(maxCount);
            searchInFileMatchCallable.setPattern(pattern);
            searchInFileMatchCallable.setContextBeforeReal(contextBeforeReal);
            searchInFileMatchCallable.setContextAfterReal(contextAfterReal);
            searchInFileMatchCallable.setPattern(pattern);

            final List<ContainerInfoSearchFileMatcherGitRepo> result = searchInFileMatchCallable.call();
            if (result.isEmpty()) {
                return null;
            }

            final String filename = GitRepoUtils.cleanRawFilePath(
                    file.toString(),
                    containerGitRepoMeta.group(),
                    containerGitRepoMeta.project());

            return new ContainerInfoSearchFileGitRepo(
                    filename,
                    FileSystemUtils.getExtensionFromRawStrFile(file.toString()),
                    GitRepoUtils.getLinkForFile(
                            containerGitRepoMeta,
                            filename,
                            currentBranch),
                    result);
        }
    }

}
