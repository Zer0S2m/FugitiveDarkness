package com.zer0s2m.fugitivedarkness.provider.git;

import java.nio.file.Path;

public record ContainerInfoRepo(

        String host,

        String group,

        String project,

        String link,

        Path source

) { }
