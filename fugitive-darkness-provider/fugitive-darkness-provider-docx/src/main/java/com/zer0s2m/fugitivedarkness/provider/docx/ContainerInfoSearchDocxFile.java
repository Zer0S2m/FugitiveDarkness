package com.zer0s2m.fugitivedarkness.provider.docx;

import java.util.List;

public record ContainerInfoSearchDocxFile(

        String file,

        String pattern,

        List<ContainerInfoFileContent> found

) {
}
