package com.zer0s2m.fugitivedarkness.provider.git;

public interface SearchEngineGrepStatistics {

    /**
     * Get the number of files in the project.
     *
     * @return Count files.
     */
    int getCountFiles();

    /**
     * Get the average file processing time.
     *
     * @return Average file processing time.
     */
    long getAverageFileProcessingTime();

}
