package com.zer0s2m.fugitivedarkness.provider.git.impl;

import java.util.HashMap;
import java.util.Map;

class GitRepoCommandGrepUtils {

    public static class GitRepoCommandGrepUtilPreviewCode {

        static final int PREVIEW_CODE_LINES = 1;

        private final Map<Integer, String> previewCodes = new HashMap<>();

        public void clearPreviewCodes() {
            previewCodes.clear();
        }

        public void addPreviewCodes(final int line, final String lineCode) {
            previewCodes.put(line, lineCode);
        }

        public String getPreviewCodeLast(final int line) {
            return previewCodes.get(line - PREVIEW_CODE_LINES);
        }

        public String getPreviewCodeLast(final int line, final int previousStep) {
            return previewCodes.get(line - (PREVIEW_CODE_LINES + previousStep));
        }

        public String getPreviewCodeNext(final int line) {
            return previewCodes.get(line + PREVIEW_CODE_LINES);
        }

        public String getPreviewCodeNext(final int line, final int previousStep) {
            return previewCodes.get(line + (PREVIEW_CODE_LINES + previousStep));
        }

        public int getPreviewLineNumberLast(final int line) {
            return line - PREVIEW_CODE_LINES;
        }

        public int getPreviewLineNumberLast(final int line, final int previousStep) {
            return line - (PREVIEW_CODE_LINES + previousStep);
        }

        public int getPreviewLineNumberNext(final int line) {
            return line + PREVIEW_CODE_LINES;
        }

        public int getPreviewLineNumberNext(final int line, final int previousStep) {
            return line + (PREVIEW_CODE_LINES + previousStep);
        }

    }

}
