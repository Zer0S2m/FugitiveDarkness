package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.FileProjectCountLine;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.concurrent.Callable;

class ProjectCountLineFilesCallable implements Callable<FileProjectCountLine> {

    private Reader reader;

    private String file;

    public ProjectCountLineFilesCallable setReader(Reader reader) {
        this.reader = reader;
        return this;
    }

    public ProjectCountLineFilesCallable setFile(String file) {
        this.file = file;
        return this;
    }

    @Override
    public FileProjectCountLine call() throws Exception {
        long countLines = 0;
        try (final BufferedReader buf = new BufferedReader(reader)) {
            while (buf.readLine() != null) {
                countLines++;
            }
        }
        return new FileProjectCountLine(file, countLines);
    }

}
