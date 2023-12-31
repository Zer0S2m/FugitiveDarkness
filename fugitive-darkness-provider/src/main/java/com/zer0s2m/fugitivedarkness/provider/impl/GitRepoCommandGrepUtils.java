package com.zer0s2m.fugitivedarkness.provider.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class GitRepoCommandGrepUtils {

    public static class GitRepoCommandGrepUtilPreviewCode {

        static final int PREVIEW_CODE_LINES = 1;

        private final Map<Integer, String> previewCodes = new HashMap<>();

        private final Set<Integer> usedLineCodes = new HashSet<>();

        public void clearPreviewCodes() {
            previewCodes.clear();
        }

        public void clearUsedLineCodes() {
            usedLineCodes.clear();
        }

        public String addPreviewCodes(final int line, final String lineCode) {
            return previewCodes.put(line, lineCode);
        }

        public String getPreviewCodeLast(final int line) {
            return previewCodes.get(line - PREVIEW_CODE_LINES);
        }

        public String getPreviewCodeLast(final int line, final int previousStep) {
            return previewCodes.get(line - (PREVIEW_CODE_LINES + previousStep));
        }

        public int getPreviewLineNumberLast(final int line) {
            return line - PREVIEW_CODE_LINES;
        }

        public int getPreviewLineNumberLast(final int line, final int previousStep) {
            return line - (PREVIEW_CODE_LINES + previousStep);
        }

        public void addUsedLineCodes(final int lineCode) {
            usedLineCodes.add(lineCode);
        }

    }

}
