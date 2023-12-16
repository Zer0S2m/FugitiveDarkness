package com.zer0s2m.fugitivedarkness.provider;

import java.nio.file.Path;

public record ContainerInfoRepo(

        String host,

        String group,

        String project,

        Path source

) { }
