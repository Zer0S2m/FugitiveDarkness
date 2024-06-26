package com.zer0s2m.fugitivedarkness.plugin.job.impl;

import com.zer0s2m.fugitivedarkness.plugin.job.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A manager for performing scheduled tasks of various types.
 * <p>Supported types:</p>
 * <ul>
 *     <li>{@link GitTypeJob#PERMANENT}</li>
 *     <li>{@link GitTypeJob#ONETIME_USE}</li>
 * </ul>
 */
public class JobManagerImpl extends JobRunnableRuleAbstract implements JobManager {

    private final Logger logger = LoggerFactory.getLogger(JobManagerImpl.class);

    private final GitJobManager gitJobManager = new GitJobManagerImpl();

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
        setProperties(properties);

        try {
            run(type);
        } catch (JobValidationRuleException e) {
            logger.warn("Failed to start a deferred task [" + e.getMessage() + "]");
            return;
        }

        if (type.equals(GitTypeJob.ONETIME_USE)) {
            JobManagerCheckProperties.checkPropertiesGitJobType(properties);

            gitJobManager.call(
                    (String) properties.get("group"),
                    (String) properties.get("project"),
                    GitTypeJob.ONETIME_USE);
        } else if (type.equals(GitTypeJob.PERMANENT)) {
            JobManagerCheckProperties.checkPropertiesGitJobType(properties);

            gitJobManager.call(
                    (String) properties.get("group"),
                    (String) properties.get("project"),
                    GitTypeJob.PERMANENT);
        } else {
            throw new JobNotFoundExecutorException(
                    "Task executor of the type was not found "
                            + "[" + type + "]");
        }
    }

    @Override
    protected void setProperties(Map<String, Object> properties) {
        this.properties = properties;
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
