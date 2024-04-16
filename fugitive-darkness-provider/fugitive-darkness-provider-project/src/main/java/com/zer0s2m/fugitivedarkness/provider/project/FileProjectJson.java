package com.zer0s2m.fugitivedarkness.provider.project;

import java.util.Collection;

public record FileProjectJson(

        String path,

        String filename,

        boolean isFile,

        boolean isDirectory,

        Collection<FileProjectJson> children
        
) {}
