package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoSearchFileGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoSearchFileMatcherGitRepo;
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

            final List<ContainerInfoSearchFileMatcherGitRepo> result = searchInFileMatchCallable.call();
            if (result.isEmpty()) {
                return null;
            }

            final String[] splitFile = file.toString().split("/");
            final String splitFileLast = splitFile[splitFile.length - 1];
            final String[] fileExtensionSplit = splitFileLast.split("//.");

            return new ContainerInfoSearchFileGitRepo(
                    file.toString(),
                    fileExtensionSplit[fileExtensionSplit.length - 1],
                    GitRepoUtils.getLinkForFile(
                            containerGitRepoMeta,
                            file.toString(),
                            "master"),
                    result);
        }
    }

}
