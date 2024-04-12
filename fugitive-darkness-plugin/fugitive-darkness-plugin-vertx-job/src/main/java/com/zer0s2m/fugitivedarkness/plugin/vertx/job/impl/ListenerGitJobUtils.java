package com.zer0s2m.fugitivedarkness.plugin.vertx.job.impl;

import com.zer0s2m.fugitivedarkness.common.mapper.FieldBuilder;
import com.zer0s2m.fugitivedarkness.models.GitJobModel;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

interface ListenerGitJobUtils {

    static Map<String, Object> collectProperties(GitJobModel gitJob) {
        final Map<String, Object> properties = new HashMap<>();

        properties.put("group", gitJob.getGitRepoGroup());
        properties.put("project", gitJob.getGitRepoProject());

        return properties;
    }

    static List<GitJobModel> getGitJobsFromRowSet(RowSet<Row> rowSet, FieldBuilder<Row> builder) {
        final List<GitJobModel> gitJobs = new ArrayList<>();

        StreamSupport.stream(rowSet.spliterator(), false)
                .forEach(row -> {
                    try {
                        gitJobs.add((GitJobModel) builder
                                .build(
                                        row,
                                        GitJobModel.class,
                                        List.of("id", "group_", "project", "cron", "next_run_at")));
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return gitJobs;
    }

}
