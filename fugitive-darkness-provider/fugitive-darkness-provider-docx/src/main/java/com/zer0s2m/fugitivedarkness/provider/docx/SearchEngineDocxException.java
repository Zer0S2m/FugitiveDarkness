package com.zer0s2m.fugitivedarkness.provider.docx;

/**
 * The general exception for the search engine is docx files.
 */
public abstract class SearchEngineDocxException extends Exception {

    public SearchEngineDocxException(String message) {
        super(message);
    }

}
