package com.zer0s2m.fugitivedarkness.provider.project;

import java.io.Reader;

public record ContainerInfoReader(

        String path,

        Reader reader

) {
}
