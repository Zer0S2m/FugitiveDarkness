package com.zer0s2m.fugitivedarkness.provider.docx;

/**
 * The general exception for the search engine is that the manager for managing the docx file is closed.
 */
public class SearchEngineDocxCloseManagerException extends SearchEngineDocxException {

    public SearchEngineDocxCloseManagerException(String message) {
        super(message);
    }

}
