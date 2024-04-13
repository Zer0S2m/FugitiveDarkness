package com.zer0s2m.fugitivedarkness.plugin.job;

import java.util.Map;

/**
 * An interface for running scheduled tasks of various types.
 * <p>Supported types:</p>
 * <ul>
 *     <li>{@link GitTypeJob#PERMANENT}</li>
 *     <li>{@link GitTypeJob#ONETIME_USE}</li>
 * </ul>
 */
public interface JobManager extends JobRunnableRule {

    /**
     * Run a task with a certain type and a certain set of properties.
     *
     * @param type       The type of scheduled task.
     * @param properties A set of properties required to run a specific scheduled task.
     * @throws JobException                  A general exception.
     * @throws JobMissingPropertiesException There are no required parameters to start a scheduled task.
     */
    void call(Object type, Map<String, Object> properties) throws JobException;

}
