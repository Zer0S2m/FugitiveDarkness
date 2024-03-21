package com.zer0s2m.fugitivedarkness.provider.docx;

import java.util.Collection;

public record ContainerInfoFileContent(

        int lineNumber,

        String line,

        Collection<ContainerInfoFileContentMatch> matchers

) {
}
