package com.zer0s2m.fugitivedarkness.provider.project;

public record FileProject(

        String path,

        boolean isFile,

        boolean isDirectory

) {

    public FileProject copy(String path) {
        return new FileProject(
                path,
                isFile,
                isDirectory);
    }

}
