package com.zer0s2m.fugitivedarkness.plugin.job.impl;

import com.zer0s2m.fugitivedarkness.plugin.job.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * A manager for performing scheduled tasks of various types.
 * <p>Supported types:</p>
 * <ul>
 *     <li>{@link GitTypeJob#PERMANENT}</li>
 *     <li>{@link GitTypeJob#ONETIME_USE}</li>
 * </ul>
 */
public class JobManagerImpl implements JobManager {

    /**
     * Run a task with a certain type and a certain set of properties.
     *
     * @param type       The type of scheduled task.
     * @param properties A set of properties required to run a specific scheduled task.
     * @throws JobException                  A general exception.
     * @throws JobMissingPropertiesException There are no required parameters to start a scheduled task.
     * @throws JobNotFoundExecutorException  The scheduled task type was not found.
     */
    @Override
    public void call(Object type, Map<String, Object> properties) throws JobException {
        if (type.equals(GitTypeJob.ONETIME_USE)) {
            JobManagerCheckProperties.checkPropertiesGitJobType(properties);

            GitJobExecutor executor = new GitJobExecutorOnetimeUseImpl();
            executor.setGroup((String) properties.get("group"));
            executor.setProject((String) properties.get("project"));
            executor.run();
        } else if (type.equals(GitTypeJob.PERMANENT)) {
            JobManagerCheckProperties.checkPropertiesGitJobType(properties);

            GitJobExecutor executor = new GitJobExecutorPermanentImpl();
            executor.setGroup((String) properties.get("group"));
            executor.setProject((String) properties.get("project"));
            executor.run();
        }

        throw new JobNotFoundExecutorException(
                "Task executor of the type was not found "
                        + "[" + type + "]");
    }

    static class JobManagerCheckProperties {

        static void checkPropertiesGitJobType(Map<String, Object> properties) throws JobException {
            Collection<String> missingProperties = new ArrayList<>();

            if (!properties.containsKey("group")) {
                missingProperties.add("group");
            }
            if (!properties.containsKey("project")) {
                missingProperties.add("project");
            }

            if (!missingProperties.isEmpty()) {
                throw new JobMissingPropertiesException(
                        "Required properties are missing to run the task " + missingProperties);
            }
        }

    }

}
